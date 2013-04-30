
################################################################################
#################### Functions for Monte Carlo estimation ######################
################################################################################

# Cumulative average
cumav <- function(vec)
{
  n <- length(vec)
  cumsum(vec) / (1:n);
}

# Typical result of estimating function is a list with following fields:
# est -- estimated value
# sd -- standard deviation
# se -- standard error

# Function calculates several values for vector of samples
# Y -- vector of samples.
# return -- list of values described above.
samples <- function(Y)
{
  n <- length(Y)
  av <- mean(Y)
  stddev <- sd(Y)
  se <- stddev / sqrt(n)
  list ( est = av,
         cumav = cumav(Y),
         sd = stddev,
         se = se
  )  
}

# Function returns value of crude Monte Carlo estimator for given parameters.
# n -- number of simulations.
# rfun -- function for generating random values, it shoud take as an argument
#   natural number k and return a vector of k independent random values.
# return -- result of estimation described above.
crudeMC <- function(n, rfun)
{
  Y <- rfun(n)
  samples(Y)
}

# Function returns value of antithetic Monte Carlo method for given parameters.
# n -- number of simulations.
# rfun -- function for generating random values, it shoud take as an argument
#   natural number k and return a list with fields fst and snd. Each field is a
#   vector of k independent random values.
# return -- result of estimation described above.
antitheticVariates <- function(n, rfun)
{
  Y <- rfun(n)
  Y <- ( Y$fst + Y$snd ) / 2
  samples(Y)
}

# Function returns value of control variates Monte Carlo estimator for given
# parameters.
# n -- number of simulations.
# rfun -- function for generating random values, it shoud take as an argument
#   natural number k and return a list with two fields: X and Y. Each field is a
#   vector of k independent random values.
# EX -- expected value of X
# return -- result of estimation described above.
controlVariates <- function(n, rfun, EX)
{
  res <- rfun(n)
  Y <- res$Y
  X <- res$X
  c <- -cov(X,Y)/var(X)
  Z <- Y + c*(X - EX)
  samples(Z)
}

################################################################################
################ Below is code testing Monte Carlo methods #####################
############## We will calculate \int_0^1 e^x dx by simulation #################
################################################################################


# Creates functions which will perform random sampling.
# result -- list with fields
#   cmcFun -- drawing function for crude Monte Carlo
#   avFun -- drawing function for antithetic variates
#   cvFun -- drawing function for control variates
creatingDrawingFuns <- function()
{
  cmcFun <- function(n)
  {
    exp( runif(n) )
  }
  avFun <- function(n)
  {
    U <- runif(n)
    list (
      fst = exp(U),
      snd = exp(1-U)
    ) 
  }
  cvFun <- function(n)
  {
    U <- runif(n)
    list (
      Y = exp(U),
      X = U
    )
  }
  list( cmcFun = cmcFun,
        avFun = avFun,
        cvFun = cvFun
      )
}

# Function calculates \int_0^1 e^x dx by simulation using several MC methods.
# ns -- vectors with numbers of simulation. For crude MC two times as much
#    -- simulations will be perfomered.
# return -- data frame is result. One row is for n from ns. Columns are
#   estimated expect values and standard errors for all methods.
compareMC <- function(ns, toFile=FALSE)
{
  F <- creatingDrawingFuns()
  compareOnce <- function(n)
  {
    cmcRes <- crudeMC(2*n, F$cmcFun)
    avRes <- antitheticVariates(n, F$avFun)
    cvRes <- controlVariates(n, F$cvFun, 0.5)
    list(
      n = n,
      CMC.est = cmcRes$est,
      CMC.se = cmcRes$se,
      AV.est = avRes$est,
      AV.se = avRes$se,
      CV.est = cvRes$est,
      CV.se = cvRes$se
    )
  }
  M <- t(sapply(ns, function(n) compareOnce(n)))
  M[] <- lapply(M, round, 6)
  if (toFile)
  {
    library(sfsmisc)
    mat2tex(M, file="tmp.tex", digits=6)
  }
  M
}

# For every method K times runs estimation with n replications (in case of CMC
# 2n replications).
# K -- how many times run estimation.
# n -- how many repliations in one estimation
boxesMC <- function(K=100, n=10000)
{
  F <- creatingDrawingFuns()
  library(ggplot2)
  cmc <- sapply(1:K, function(foo) crudeMC(2*n, F$cmcFun)$est)
  av  <- sapply(1:K, function(foo) antitheticVariates(n, F$avFun)$est)
  cv  <- sapply(1:K, function(foo) controlVariates(n, F$cvFun, 0.5)$est)
  data <- data.frame(
    est = c(cmc, av, cv),
    method = c(rep("CMC", K), rep("AV", K), rep("CV", K))
    )
  qplot(method, est, data=data, ylab="Estimated value", geom="boxplot") +
    geom_jitter(size=0.75)
}

convergence <- function(n=10000)
{
  F <- creatingDrawingFuns()
  cmc <- crudeMC(2*n, F$cmcFun)$cumav
  cmc <- cmc[(1:n)*2];
  av <- antitheticVariates(n, F$avFun)$cumav
  cv <- controlVariates(n, F$cvFun, 0.5)$cumav
  data <- data.frame(
    simulated = 50:n,
    cmc = cmc[-(1:49)],
    av = av[-(1:49)],
    cv = cv[-(1:49)]
    )
  require("reshape")
  require("ggplot2")
  
  data_long <- melt(data, id="simulated")  # convert to long format
  names(data_long) <- c("replicated pairs", "method", "estimated value")
  
  ggplot(data=data_long, ylim=c(1.68, 1.76),
         aes(x=`replicated pairs`, y=`estimated value`, colour=method)) +
    geom_line() +
    geom_hline(yintercept=exp(1)-1, show_guide=TRUE)
}
