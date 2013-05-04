source("MonteCarlo.R")

fmt <- function(digits)
{
  function(x) format(x, nsmall = digits,scientific = FALSE)
}

# Function compares CMC, AV and CV.
# ns -- vectors with numbers of simulation. For crude MC two times as much
#    -- simulations will be perfomered.
# cmcFun -- drawing function for CMC estimator,
# avFun -- drawing function for AV estimator,
# cvFun -- drawing function for CV estimator,
# CV_EX -- expected value of control variable,
# return -- data frame is result. One row is for n from ns. Columns are
#   estimated expect values and standard errors for all methods.
compareMC <- function(ns, cmcFun, avFun, cvFun, CV_EX, toFile=FALSE, digits=6, seed=NULL)
{
  runCMC <- function(n)
  {
    cmcRes <- crudeMC(2*n, cmcFun, seed)
    list(
      n = n,
      CMC.est = cmcRes$est,
      CMC.se = cmcRes$se
    )
  }
  runAV <- function(n)
  {
    avRes <- antitheticVariates(n, avFun, seed)
    list(
      AV.est = avRes$est,
      AV.se = avRes$se
    )    
  }
  runCV <- function(n)
  {
    cvRes <- controlVariates(n, cvFun, CV_EX, seed)
    list(
      CV.est = cvRes$est,
      CV.se = cvRes$se
    )
  }
  
  M <- t(sapply(ns, function(n) runCMC(n)))
  M <- cbind(M, t(sapply(ns, function(n) runAV(n))) )
  M <- cbind(M, t(sapply(ns, function(n) runCV(n))) )
  M[] <- lapply(M, round, digits)
  if (toFile)
  {
    library(sfsmisc)
    mat2tex(M, file="tmp.tex", digits=digits)
  }
  M
}

# For every method K times runs estimation with n replications (in case of CMC
# 2n replications).
# K -- how many times run estimation.
# n -- how many repliations in one estimation
# cmcFun -- drawing function for CMC estimator,
# avFun -- drawing function for AV estimator,
# cvFun -- drawing function for CV estimator,
# CV_EX -- expected value of control variable,
# return -- box plot
boxesMC <- function(K=100, n=10000, cmcFun, avFun, cvFun, CV_EX, seed=NULL)
{
  library(ggplot2)
  cat("Function boxes MC\nCMC: ")
  cmc <- sapply(1:K, function(i) { cat(paste(i,"")); crudeMC(2*n, cmcFun)$est })
  cat("\nAV: ")
  av  <- sapply(1:K, function(i) { cat(paste(i,"")); antitheticVariates(n, avFun)$est })
  cat("\nCV: ")
  cv  <- sapply(1:K, function(i) { cat(paste(i,"")); controlVariates(n, cvFun, CV_EX)$est })
  cat("\n")
  data <- data.frame(
    est = c(cmc, av, cv),
    method = c(rep("CMC", K), rep("AV", K), rep("CV", K))
  )
  qplot(method, est, data=data, ylab="Estimated value", geom="boxplot",
        fill=method, xlim=c("CMC", "AV", "CV")) +
    geom_jitter(size=0.75) + theme_bw() +
    scale_y_continuous(labels=fmt(2))
}

# Tests speed of convergence.
# n -- number of replications,
# cmcFun -- drawing function for CMC estimator,
# avFun -- drawing function for AV estimator,
# cvFun -- drawing function for CV estimator,
# CV_EX -- expected value of control variable,
# realValue -- real value of estimated object,
# return -- plot with speed of convargence
convergence <- function(n=100000, cmcFun, avFun, cvFun, CV_EX, realValue, seed=NULL)
{
  cmc <- crudeMC(2*n, cmcFun)$cumav
  cmc <- cmc[(1:n)*2];
  av <- antitheticVariates(n, avFun)$cumav
  cv <- controlVariates(n, cvFun, CV_EX)$cumav
  omit <- 0.01*n
  data <- data.frame(
    simulated = (omit+1):n,
    av = av[-(1:omit)],
    cmc = cmc[-(1:omit)],
    cv = cv[-(1:omit)]
  )
  data <- data[seq(1, (n-omit), by = 100), ]
  require("reshape")
  require("ggplot2")
  
  data_long <- melt(data, id="simulated")  # convert to long format
  names(data_long) <- c("replicated pairs", "method", "estimated value")
  
  plot <- ggplot(data=data_long, aes(x=`replicated pairs`, y=`estimated value`, colour=method)) +
    geom_line() +
    theme_bw() +
    scale_y_continuous(labels=fmt(2))
  
  if (!is.null(realValue))
    plot <- plot + geom_hline( yintercept=realValue, show_guide=TRUE  )
  plot
}
