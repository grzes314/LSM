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
  colors <- c("red", "blue", "green", "orange", "violet")
  K <- 100
  S1 <- trajectory(0, K, K)
  qp <- qplot((0:75)/K, S1[1:76], xlim=c(0,1), ylim=c(0,1.8*S1[1]),
          xlab="time", ylab="stock") + geom_line(size=2)
  qp2 <- qplot((75:100)/K, S1[76:101], size=1)
  qp <- qp + geom_line((75:100)/K, S1[76:101], size=1)
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

