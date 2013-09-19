
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



source("forThesis.R")
#*******************************************************************************
#**************************** BARRIER ******************************************
#*******************************************************************************

makeBarsForBarrier <- function(wynikiStr)
{
  conn <- textConnection(wynikiStr)
  wyniki <- read.csv(conn)
  wyniki <- data.frame(timesteps=as.factor(wyniki[,1]), barrier=as.factor(wyniki[,2]),
                       price=wyniki[,3], realPrice = wyniki[,4])
  close(conn)
  
  ggplot(data=wyniki, aes(x=barrier, y=price, fill=timesteps)) + 
    geom_bar(colour="black", stat="identity",
             position=position_dodge(),
             size=.3) +                        # Thinner lines
    geom_errorbar(aes(y=realPrice, ymax=realPrice, ymin=realPrice), colour="orange") +
    scale_fill_hue(name="Time steps") +      # Set legend title
    xlab("Barrier level") + ylab("Price") + # Set axis labels
    my_theme()
}

################################################################################
########################## European up-and-in call, E=100, S=100, vol=0.2
wynikiStr <- "
timesteps, barrier, simPrice, realPrice
1000, 120, 9.22, 9.275
250, 120, 9.12, 9.275
50, 120, 8.91, 9.275
1000, 140, 4.68, 4.74
250, 140, 4.51, 4.74
50, 140, 4.30, 4.74
1000, 160, 1.539, 1.553
250, 160, 1.428, 1.553
50, 160, 1.33, 1.553
"
makeBarsForBarrier(wynikiStr) + ggtitle("Up-and-in call")

################################################################################
########################## European down-and-out put, E=100, S=100, vol=0.4
wynikiStr <- "
timesteps, barrier, simPrice, realPrice
1000, 70, 1.967, 1.861
250, 70, 2.079, 1.861
50, 70, 2.378, 1.861
1000, 60, 5.028, 4.903
250, 60, 5.229, 4.903
50, 60, 5.596, 4.903
1000, 50, 8.828, 8.717
250, 50, 8.938, 8.717
50, 50, 9.253, 8.717
"
makeBarsForBarrier(wynikiStr) + ggtitle("Down-and-out put")

#*******************************************************************************
#*********************** ASIAN & LOOKBACK **************************************
#*******************************************************************************

makeBarsForOther <- function(wynikiStr)
{
  conn <- textConnection(wynikiStr)
  wyniki <- read.csv(conn)
  wyniki <- data.frame(timesteps=as.factor(wyniki[,1]), exercise=wyniki[,2],
                       price=wyniki[,3])
  close(conn)
  
  ggplot(data=wyniki, aes(x=exercise, y=price, fill=timesteps)) + 
    geom_bar(colour="black", stat="identity",
             position=position_dodge(),
             size=.3) +                        # Thinner lines
    scale_fill_manual(name="Time steps", values=c("#F8766D", "#00BA38", "#619CFF",
                                                   "#F0F000", "#E000E0", "#00E0E0")) +
    xlab("Exercise style") + ylab("Price") + # Set axis labels
    my_theme()
}

################################################################################
########################## Asian call S=100, vol=0.2, r=0.05
wynikiStr <- "
timesteps, exercise, price
500, European, 5.871
200, European, 5.853
100, European, 5.854
50, European, 5.853
500, American, 7.500
200, American, 7.489
100, American, 7.406
50, American, 7.284
"
makeBarsForOther(wynikiStr) + ggtitle("Asian call")

################################################################################
########################## Lookback put S=100, vol=0.2, r=0.05
wynikiStr <- "
timesteps, exercise, price
500, European, 13.691
200, European, 13.357
100, European, 12.995
50, European, 12.510
500, American, 14.558
200, American, 14.208
100, American, 13.819
50, American, 13.350
"
makeBarsForOther(wynikiStr) + ggtitle("Lookback put")


#*******************************************************************************
#*********************** HISTORYCZNE **************************************
#*******************************************************************************

makePricesChart <- function(wynikiStr)
{
  conn <- textConnection(wynikiStr)
  wyniki <- read.csv(conn)
  close(conn)
  wyniki <- melt(wyniki, id="strike")
  
  ggplot(data=wyniki, aes(x=strike, y=value, colour=variable)) + 
    geom_line() + 
    xlab("strike") + ylab("Price") +
    my_theme() +
    guides(color=guide_legend(title=NULL))
}

####################################### FACEBOOK PUT
wynikiStr <- "
strike, real, calculated
55, 13.95, 14.840
50, 9.45, 10.676
45, 5.95, 7.013
40, 3.28, 4.043
35, 1.37, 1.924
30, 0.48, 0.684
25, 0.13, 0.154
20, 0.06, 0.016
15, 0.03, 0.0004
"
makePricesChart(wynikiStr)

####################################### GOOGLE CALL
wynikiStr <- "
strike, real, calculated
500, 391.5, 347.210
555, 318.6, 292.245
600, 292, 247.290
650, 214.5, 197.520
700, 198.5, 148.758
750, 130.83, 103.285
800, 75, 64.692
850, 43.5, 35.996
900, 24.1, 17.673
950, 13.2, 7.662
1000, 6.4, 2.952
1050, 2.4, 1.019
1100, 1.75, 0.319
1150, 0.9, 0.091
1200, 0.29, 0.024
"
makePricesChart(wynikiStr)


impliedVol <- "
strike, vol
500, 1.13
600, 1.12
700, 0.64
800, 0.26
900, 0.23
1000, 0.24
1100, 0.23
1200, 0.25
"