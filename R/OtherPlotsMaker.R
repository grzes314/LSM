
################################################################################
################## rysunek zmiennych skorelowanych #############################

boxMuller <- function()
{
  U <- runif(2)
  root <- sqrt( -2 * log(U[1]) )
  N1 <- root * cos(2 * pi * U[2])
  N2 <- root * sin(2 * pi * U[2])
  c(N1, N2)
}

randomCorr <- function(corr)
{
  N <- boxMuller()
  c(
    N[1],
    corr * N[1] + sqrt(1 - corr^2) * N[2]
  )
}


makeOneScatterPlot <- function(corr)
{
  pairs <- data.frame(t(sapply(1:1000, function(foo) randomCorr(corr))))
  names(pairs) <- c("Z1", "Z2")
  qplot(x=Z1, y=Z2, data=pairs) +
    theme_bw(base_size=16) + geom_point()
}

drawCorrelationIllustration <- function()
{
  require(grid)
  
  vp1 <- viewport(width=0.5, height=0.5, x=0.25, y=0.75)
  wyk1 <- makeOneScatterPlot(-0.9)
  
  vp2 <- viewport(width=0.5, height=0.5, x=0.75, y=0.75)
  wyk2 <- makeOneScatterPlot(-0.5)
  
  vp3 <- viewport(width=0.5, height=0.5, x=0.25, y=0.25)
  wyk3 <- makeOneScatterPlot(0)
  
  vp4 <- viewport(width=0.5, height=0.5, x=0.75, y=0.25)
  wyk4 <- makeOneScatterPlot(0.5)
  
  vp <- viewport()
  print(wyk1, vp=vp1)
  print(wyk2, vp=vp2)
  print(wyk3, vp=vp3)
  print(wyk4, vp=vp4)
}


#################################################################
## Rysunek na potrzeby prezentacji opcji barierowych ############
source("EuOptions.R")

plotTrsForBarrierExplanation <- function(K = 50, S0 = 100, T=1.0)
{  
  rynek <- list(
    r = 0,
    t = 0,
    S = S0,
    vol = 0.3,
    mu = 0,
    q = 0
  )
  
  hit <- trajectory(rynek, T, K)
  while (max(hit) < 130 || hit[50] > 130 || hit[50] < 100)
    hit <- trajectory(rynek, T, K)
  
  outMoney <- trajectory(rynek, T, K)
  while (max(outMoney) > 130 || outMoney[50] > 100)
    outMoney <- trajectory(rynek, T, K)
  
  inMoney <- trajectory(rynek, T, K)
  while (max(inMoney) > 130 || inMoney[50] < 110)
    inMoney <- trajectory(rynek, T, K)
  
  trs <- cbind(hit, outMoney, inMoney)
  
  
  trs <- cbind( (0:K)*(T/K), trs)  # adding labels of x axis
  colnames(trs) <- c("time", sapply(1:3, function(i) paste("tr", i)))
  trs <- data.frame(trs)
  trs <- melt(trs, id="time")
  
  ggplot(data=trs) + xlab("time")  + ylab("asset price") +
    geom_line( aes(x=time, y=value, color=variable), size=1  ) +
    geom_hline(aes(yintercept=100)) + 
    geom_hline(aes(yintercept=130)) + 
    theme_bw(base_size=12) + theme(legend.position="none") +
    scale_y_continuous(breaks = seq(from=50, to=200, by=10)) +
    scale_x_continuous(breaks = (0:10)/10)
}




