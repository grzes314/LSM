trajectory <- function(start, end, K, r = 0.05, vol=0.05, Sstart = 100, T = 1.0, N = NULL)
{    
  dt <- T/K
  S <- rep(NA, K+1)
  if (is.null(N))
    N <- rnorm(K)
  S[start+1] <- Sstart
  for (j in seq(from=start+2, to=end+1, by=1))
  {
    S[j] = S[j-1]*exp((r - 0.5*vol^2)*dt + N[j-1]*vol*sqrt(dt))
  }   
  S
}

require("reshape")
require("ggplot2")
LSMidea1 <- function()
{
  mainTr <- trajectory(0, 50, 100)
  rs <- c(0.4, 0.2, -0.2, -0.4)
  subTr <- sapply(1:4, function(i) trajectory(50, 75, 100, Sstart = mainTr[51], T = 0.5, r = rs[i]))
  subsubTr1 <- sapply(1:5, function(i) trajectory(75, 100, 100, Sstart = subTr[76,1], T = 0.25, r = rs[i]))
  subsubTr2 <- sapply(1:5, function(i) trajectory(75, 100, 100, Sstart = subTr[76,2], T = 0.25, r = rs[i]))
  subsubTr3 <- sapply(1:5, function(i) trajectory(75, 100, 100, Sstart = subTr[76,3], T = 0.25, r = rs[i]))
  subsubTr4 <- sapply(1:5, function(i) trajectory(75, 100, 100, Sstart = subTr[76,4], T = 0.25, r = rs[i]))
  dt <- T/100
  trs <- data.frame( cbind(time=(0:100)*dt, mainTr, subTr, subsubTr1, subsubTr2, subsubTr3, subsubTr4) )
  #colnames(trs) <- c("time", sapply(1:6, function(i) paste("tr",i)))
  trs <- melt(trs, id="time")
  
  ggplot(data=trs) + xlab("time")  + ylab("asset price") +
    geom_line( aes(x=time, y=value, group=variable) ) +
    theme_bw(base_size=14) + theme(legend.position = "none") +
    scale_y_continuous(limits = range(trs$value, na.rm=TRUE), breaks = seq(from=90, to=120, by=5)) +
    scale_x_continuous(breaks = (0:10)/10)
}

LSMidea2 <- function()
{
  K <- 100
  n <- 5
  trs <- data.frame( matrix(nrow = K+1, ncol = n+1) )
  colnames(trs) <- c("time", sapply(1:n, function(i) paste("tr", i, sep="")))
  trs$time <- (0:K) / K
  for (i in 1:n)
  {
    good <- FALSE
    while (!good)
    {
      trs[,i+1] <- trajectory(0,K,K, vol=0.3)
      if (abs(trs[61,i+1] - 100) < 10)
        good <- TRUE
    }
  }
  wers <- trs[61,]
  wers <- melt(wers, id="time")
  trs <- melt(trs, id="time")
  ggplot(data=trs) + xlab("time")  + ylab("asset price") +
    geom_line( aes(x=time, y=value, color=variable) ) +
    geom_point( data=wers, aes(x=time, y=value, color=variable), size=5) +
    theme_bw(base_size=14) + theme(legend.position = "none") +
    scale_y_continuous(limits = c(60,140)) +
    scale_x_continuous(breaks = (0:10)/10) +
    geom_vline(aes(xintercept=wers[1,1]), color="#AAAAAA")
}
