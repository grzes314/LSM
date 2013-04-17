trajectory <- function(K, r = 0.05, vol=0.2, S0 = 100, T = 1.0, N = NULL)
{    
  dt <- T/K
  S <- rep(0, K)
  if (is.null(N))
    N <- rnorm(K)
  S[1] <- S0
  for (j in 2:K)
  {
    S[j] = S[j-1]*exp((r - 0.5*vol^2)*dt + N[j]*vol*sqrt(dt))
  }   
  S
}

trajectories <- function(n, r = 0.05, vol=0.2, S0 = 100, T=1.0)
{
  K <- 250
  res <- sapply(1:n, function(foo) trajectory(K, r, vol, S0, T))
  M <- max(res)
  plot((1:K)/K*T, res[,1], ylim=c(0,1.5*M), xlab="time", ylab="stock", type='l')
  for (i in 2:n)
  {
    lines((1:K)/K*T, res[,i])
  }
}

LSMidea1 <- function(r = 0.05, vol=0.3, S0 = 100, N = NULL)
{
  K <- 250
  K1 <- 200
  S1 <- trajectory(K, r, vol, S0, T=1, N=N)
  res <- sapply(1:6, function(foo) trajectory(K-K1, r, vol, S1[K1], T=0.2))
  
  plot((1:K1)/K, S1[1:K1], xlim=c(0,1), ylim=c(0,1.8*S0),
       xlab="time", ylab="stock", type='l', lwd=3)
  for (i in 1:6)
  {
    lines((K1:(K-1))/K, res[,i])
  }
  abline(v=0.8)
  abline(h=S1[K1])
  points(0.8, S1[K1], bg="black", pch=21)
}

LSMidea2 <- function(r = 0.05, vol=0.3, S0 = 100, N = NULL)
{
  colors <- c("red", "blue", "green", "orange", "violet")
  K <- 250
  K1 <- 200
  S1 <- trajectory(K, r, vol, S0, T=1, N=N)
  plot((1:K1)/K, S1[1:K1], xlim=c(0,1), ylim=c(0,1.8*S0),
       xlab="time", ylab="stock", type='l', lwd=3)
  lines(K1:K/K, S1[K1:K])
  points(0.8, S1[K1], bg="black", pch=21)
  abline(v=0.8)
  abline(h=S1[K1])
        
  nr <- 1
  while (nr < 6)
  {
    S2 <- trajectory(K, r, vol, S0, T=1)
    if (abs(S2[K1] - S1[K1])/S1[K1] < 0.2)
    {
      lines((1:K)/K, S2, col=colors[nr])
      points(0.8, S2[K1], bg=colors[nr], pch=21)
      nr = nr+1
    }
  }
}

################################################################################
#################### Functions for Monte Carlo estimation ######################
################################################################################

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
  list ( est=av,
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

# Function calculates \int_0^1 e^x dx by simulation using several MC methods.
# ns -- vectors with numbers of simulation. For crude MC two times as much
#    -- simulations will be perfomered.
compareMC <- function(ns, toFile=FALSE)
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
  
  compareOnce <- function(n)
  {
    cmcRes <- crudeMC(2*n, cmcFun)
    avRes <- antitheticVariates(n, avFun)
    cvRes <- controlVariates(n, cvFun, 0.5)
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
