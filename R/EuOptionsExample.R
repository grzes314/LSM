source("EuOptions.R")
source("MonteCarloComparision.R")
require("reshape")
require("ggplot2")

################################################################################
############### Functions for plotting assets trajectories #####################
################################################################################

plotTrajectories <- function(n, K, alpha=0.5, r = 0.05, vol=0.2, S0 = 100, T=1.0)
{  
  rynek <- list(
    r = r,
    t = 0,
    S = S0,
    vol = vol,
    mu = 0,
    q = 0
  )
  
  trs <- sapply(1:n, function(foo) trajectory(rynek, T, K))
  q3 <- sapply(1:(K+1), function(i) quantile(trs[i,], probs=0.75))
  av <- sapply(1:(K+1), function(i) mean(trs[i,]))
  q2 <- sapply(1:(K+1), function(i) quantile(trs[i,], probs=0.5))
  q1 <- sapply(1:(K+1), function(i) quantile(trs[i,], probs=0.25))
  
  trs <- cbind( (0:K)*(T/K), trs)  # adding labels of x axis
  colnames(trs) <- c("time", sapply(1:n, function(i) paste("tr", i)))
  trs <- data.frame(trs)
  trs <- melt(trs, id="time")
  
  quartiles <- data.frame(time=(0:K)*(T/K), Quartile.3rd=q3, Mean=av, Median=q2, Quartile.1st=q1)
  quartiles <- melt(quartiles, id="time")
  
  ggplot(data=trs) + xlab("time")  + ylab("asset price") +
    geom_line( aes(x=time, y=value, group=variable), alpha=alpha ) +
    geom_line( data=quartiles, aes(x=time, y=value, color=variable), size=1 ) +
    guides(color=guide_legend(title=NULL)) +
    theme_bw() + theme(legend.justification=c(0,1), legend.position=c(0,1)) +
    scale_y_continuous(breaks = seq(from=50, to=200, by=10)) +
    scale_x_continuous(breaks = (0:10)/10)
}

plotTrajectoriesAnti <- function(K, r = 0.05, vol=0.2, S0 = 100, T=1.0)
{
  rynek <- list(
    r = r,
    t = 0,
    S = S0,
    vol = vol,
    mu = 0,
    q = 0
  )
  tr <- trajectoryAnti(rynek, T=1, K=K)
  pos <- tr$pos
  neg <- tr$neg
  
  trs <- data.frame(time=(0:K)*(T/K), Positive=pos, Negative=neg)
  trs <- melt(trs, id="time")
  ggplot(data=trs) + xlab("time")  + ylab("asset price") +
    geom_line( aes(x=time, y=value, color=variable) ) +
    theme_bw()
}

################################################################################
######### Functions performing valuation of example vanilla option #############
################################################################################

vanillaExample <- function(seed = 0, toFile=FALSE)
{
  rynek <- list(
    r = 0.05,
    t = 0,
    S = 100,
    vol = 0.2,
    mu = 0,
    q = 0
  )
  opcja <- list(
    E = 130,
    T = 1,
    type = CALL
  )
  ns <- c(10^(3:5))
  payoff <- payoffVanilla(r=rynek$r, option=opcja)
  cmcFun <- funForCMC(rynek, T=1, K=1, payoff)
  avFun  <- funForAV(rynek, T=1, K=1, payoff)
  cvFun <- funForCV(rynek, T=1, K=1, payoff)
  CV_EX <- rynek$S * exp(rynek$r * opcja$T)
  print(paste("BS price:", price(rynek, opcja)))
  print( compareMC(ns, cmcFun, avFun, cvFun, CV_EX, toFile, digits=5, seed) )
  print( boxesMC(K=100, n=10000, cmcFun, avFun, cvFun, CV_EX, seed) )
  print( convergence(n=100000, cmcFun, avFun, cvFun, CV_EX, price(rynek, opcja), seed) )
  "DONE!"
}



################################################################################
######### Functions performing valuation of example barrier option #############
################################################################################

barrierExample <- function(seed = 0, toFile=FALSE)
{
  rynek <- list(
    r = 0.05,
    t = 0,
    S = 100,
    vol = 0.2,
    mu = 0,
    q = 0
  )
  opcja <- list(
    E = 100,
    T = 1,
    type = PUT,
    barrierType = UP_AND_OUT,
    barrierLevel = 115
  )
  ns <- c(10^(3:5))
  payoff <- payoffBarrier(r=rynek$r, option=opcja)
  cmcFun <- funForCMC(rynek, T=1, K=50, payoff)
  avFun  <- funForAV(rynek, T=1, K=50, payoff)
  cvFun <- funForCV_barrier(rynek, opcja, K=50, payoff)
  CV_EX <- price(rynek, opcja)
  
  print( compareMC(ns, cmcFun, avFun, cvFun, CV_EX, toFile, digits=5, seed) )
  print( boxesMC(K=100, n=10000, cmcFun, avFun, cvFun, CV_EX, seed) )
  print( convergence(n=100000, cmcFun, avFun, cvFun, CV_EX, realValue=NULL, seed) )
}

chartBarrierAndStrike1 <- function()
{
  library(rgl)
  rynek <- list(
    r = 0.05,
    t = 0,
    S = 100,
    vol = 0.2,
    mu = 0,
    q = 0
  )
  opcja <- list(
    E = 100,
    T = 1,
    type = CALL,
    barrierType = UP_AND_OUT,
    barrierLevel = 130
  )
  strikes <- seq(from=75, to=130, by=5)
  barriers <- seq(from=100, to=150, by=5) 
  ls <- length(strikes)
  lb <- length(barriers)
  val <- matrix(nrow=ls, ncol=lb)
  for (i in 1:ls)
  {
    opcja$E <- strikes[i]
    for (j in 1:lb)
    {
      opcja$barrierLevel <- barriers[j]
      val[i,j] <- priceCV_barrier(n=1000, rynek, opcja, 50,
                                  payoffBarrier(rynek$r, opcja))$est
    }
  }
  val_lim <- range(val)
  vlen <- ceiling(val_lim[2] - val_lim[1] + 1)
  colorlut <- terrain.colors(vlen)
  col <- colorlut[ val - val_lim[1]+1 ]
  open3d()
  persp3d(strikes, barriers, val, color=col, back="lines")
}

chartBarrierAndStrike2 <- function()
{
  library(rgl)
  rynek <- list(
    r = 0.05,
    t = 0,
    S = 100,
    vol = 0.2,
    mu = 0,
    q = 0
  )
  opcja <- list(
    E = 100,
    T = 1,
    type = PUT,
    barrierType = DOWN_AND_OUT,
    barrierLevel = 130
  )
  strikes <- seq(from=80, to=130, by=5)
  barriers <- seq(from=60, to=100, by=5) 
  ls <- length(strikes)
  lb <- length(barriers)
  val <- matrix(nrow=ls, ncol=lb)
  for (i in 1:ls)
  {
    opcja$E <- strikes[i]
    for (j in 1:lb)
    {
      opcja$barrierLevel <- barriers[j]
      val[i,j] <- priceCV_barrier(n=1000, rynek, opcja, 50,
                                  payoffBarrier(rynek$r, opcja))$est
    }
  }
  val_lim <- range(val)
  vlen <- ceiling(val_lim[2] - val_lim[1] + 1)
  colorlut <- terrain.colors(vlen)
  col <- colorlut[ val - val_lim[1]+1 ]
  open3d()
  persp3d(strikes, barriers, val, color=col, back="lines")
}


