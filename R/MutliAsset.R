source("MonteCarlo.R")
source("MonteCarloComparision.R")
require("reshape")
require("ggplot2")

CALL <- 0
PUT <- 1

multiAssetTrajectory <- function(rynek, T, K)
{
  dt <- T/K
  L <- t( chol(rynek$corr) )
  means <- (rynek$r - rynek$vol * rynek$vol/2) * dt
  sds <- rynek$vol*sqrt(dt)
  
  d <- rynek$d
  
  S <- matrix(nrow=d, ncol=K+1)
  R <- matrix(nrow=d, ncol=K+1)
  for (i in 1:d)
    S[i,1] <- R[i,1] <-rynek$S[i]
  for (k in 2:(K+1))
  {
    Z <- rnorm(d)
    Z <- L %*% Z
    for (i in 1:d)
    {
      S[i,k] <- S[i,k-1] * exp(means[i] + Z[i]*sds[i])
      R[i,k] <- R[i,k-1] * exp(means[i] - Z[i]*sds[i])
    }
  }
  list(pos=S, neg=R)
}

funForCMC <- function(rynek, T, K, payoff)
{
  function(n)
  {
    sapply( 1:n, function(foo) payoff(multiAssetTrajectory(rynek, T, K)$pos) )
  }
}

funForAV <- function(rynek, T, K, payoff)
{
  function(n)
  {
    res <- sapply(1:n, function(foo) {
      sc <- multiAssetTrajectory(rynek, T, K)
      c( payoff(sc$pos), payoff(sc$neg) )
    })
    list( fst = res[1,], snd = res[2,] )
  }
}

funForCV <- function(rynek, T, K, basket, payoff)
{
  function(n)
  {
    res <- sapply(1:n, function(foo) {
      sc <- multiAssetTrajectory(rynek, T, K)$pos
      c( payoff(sc),
         sum( sapply(1:rynek$d, function(i) basket[i]*sc[i,K+1]) )
      )
    })
    list( Y = res[1,], X = res[2,] )
  }
}


payoffBasket <- function(rynek, basket, type, T, E)
{
  function(S) #S is genrated scenario, trajectories in rows
  {
    last <- length(S[1,])
    sum <- 0
    for (i in 1:rynek$d)
      sum <- sum + basket[i] * S[i,last]
    if (type == CALL)
      return( exp(-T*rynek$r) * max(sum - E, 0) )
    else return( exp(-T*rynek$r) * max(E - sum, 0) )
  }  
}

################################################################################
############################# Example usage ####################################
################################################################################

createRynek <- function()
{
  list(
    r = 0.05,
    t = 0,
    d = 3,
    S = c(10,50,100),
    vol = c(0.4, 0.2, 0.3),
    corr = matrix( c(   1,  0.8, -0.8,
                      0.8,    1, -0.8,
                     -0.8,  -0.8,   1), byrow=TRUE, nrow=3 )
  )
}

basketExample <- function(seed = 0, toFile=FALSE)
{
  rynek <- createRynek()
  E <- 90
  basket <- c(10, -2, 1)
  type <- CALL
  ns <- c(10^(3:5))
  payoff <- payoffBasket(rynek, basket, type, 1, E)
  cmcFun <- funForCMC(rynek, T=1, K=1, payoff)
  avFun  <- funForAV(rynek, T=1, K=1, payoff)
  cvFun <- funForCV(rynek, T=1, K=1, basket, payoff)
  CV_EX <- 0
  for (i in 1:3)
    CV_EX <- CV_EX + basket[i]*rynek$S[i]
  CV_EX <- CV_EX * exp(rynek$r * T)
  
  print( compareMC(ns, cmcFun, avFun, cvFun, CV_EX, toFile, digits=5, seed) )
  print( boxesMC(K=100, n=10000, cmcFun, avFun, cvFun, CV_EX, seed) )
  print( convergence(n=100000, cmcFun, avFun, cvFun, CV_EX, NULL, seed) )
  "DONE!"
}

plotMultiAsset <- function()
{
  rynek <- list(
    r = 0.05,
    t = 0,
    d = 3,
    S = c(100,50,50),
    vol = c(0.3, 0.4, 0.4),
    mu = c(0,0),
    q = c(0,0),
    corr = matrix( c(   1,  0.8, -0.8,
                        0.8,    1,    -0.8,
                        -0.8,    -0.8,    1), byrow=TRUE, nrow=3 )
  )
  K <- 250
  T <- 1
  tr <- multiAssetTrajectory(rynek, T=T, K=K)$pos
  trs <- data.frame(time=(0:K)*(T/K), t(tr))
  trs <- melt(trs, id="time")
  ggplot(data=trs) + xlab("time")  + ylab("assets prices") +
    geom_line( aes(x=time, y=value, color=variable) ) +
    theme_bw() + guides(color=FALSE)
}

twoOptions <- function()
{
  rynek <- list(
    r = 0.05,
    t = 0,
    d = 2,
    S = c(100,100),
    vol = c(0.2, 0.2),
    corr = matrix( c( 1,  -0.9,
                    -0.9, 1), byrow=TRUE, nrow=2 )
  )
  payoff <- function(S) #S is genrated scenario, trajectories in rows
  {
      last <- length(S[1,])
      exp(-T*rynek$r) * (max(0, S[1,last] - 100) + max(0, 100 - S[2,last]))
  }
  avFun  <- funForAV(rynek, T=1, K=1, payoff)
  avRes <- antitheticVariates(1000000, avFun)
  list( avRes$est, avRes$se )
}
