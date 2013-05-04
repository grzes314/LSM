
################################################################################
################## Functions for testing random generators #####################
################################################################################

boxMuller <- function()
{
  U <- runif(2)
  root <- sqrt( -2 * log(U[1]) )
  N1 <- root * cos(2 * pi * U[2])
  N2 <- root * sin(2 * pi * U[2])
  c(N1, N2)
}

polarRejection <- function()
{
  U <- runif(2)
  V <- 2*U - 1
  W <- sum(V^2)
  if (W > 1)
    return (polarRejection())
  root <- sqrt( -2 * log(W) / W )
  N1 <- root * V[1]
  N2 <- root * V[2]
  list(fst = N1, snd = N2)
}

testNormalGenerators <- function(n)
{
  bm <- system.time( for (i in 1:n) boxMuller() )
  pr <- system.time( for (i in 1:n) polarRejection() )
  print(paste("Box-Muller:", round(bm[1], 2), "seconds."))
  print(paste("Polar rejection method:", round(pr[1], 2), "seconds."))
}

################################################################################
# inne podejscie

boxMuller <- function(n)
{
  U1 <- runif(n)
  U2 <- runif(n)
  roots <- sqrt( -2 * log(U1) )
  N1 <- roots * cos(2 * pi * U2)
  N2 <- roots * sin(2 * pi * U2)
  c(N1, N2)
}

polarRejection <- function(n)
{
  V1 <- 2*runif(1.5*n) - 1
  V2 <- 2*runif(1.5*n) - 1
  W <- V1^2 + V2^2
  hit <- which(W <= 1)
  V1 <- (V1[hit])[1:n]
  V2 <- (V2[hit])[1:n]
  W <- (W[hit])[1:n]
  root <- sqrt( -2 * log(W) / W )
  N1 <- root * V1
  N2 <- root * V2
  c(N1, N2)
}

testNormalGenerators <- function(n)
{
  bm <- system.time( boxMuller(n) )
  pr <- system.time( polarRejection(n) )
  print(paste("Box-Muller:", round(bm[1], 2), "seconds."))
  print(paste("Polar rejection method:", round(pr[1], 2), "seconds."))
}

################################################################################
################## rysunek zmiennych skorelowanych #############################
# zakladamy ze boxMuller dziala tak jak w pierwszym podejsciu
randomCorr <- function(corr)
{
  N <- boxMuller()
  c(
    N[1],
    corr * N[1] + sqrt(1 - corr^2) * N[2]
  )
}

draw <- function()
{
  require(grid)
  
  vp1 <- viewport(width=0.5, height=0.5, x=0.25, y=0.75)
  pairs <- data.frame(t(sapply(1:1000, function(foo) randomCorr(-0.8))))
  names(pairs) <- c("Z1", "Z2")
  wyk1 <- qplot(x=Z1, y=Z2, data=pairs) +
    geom_point()
  
  
  vp2 <- viewport(width=0.5, height=0.5, x=0.75, y=0.75)
  pairs <- data.frame(t(sapply(1:1000, function(foo) randomCorr(-0.4))))
  names(pairs) <- c("Z1", "Z2")
  wyk2 <- qplot(x=Z1, y=Z2, data=pairs) +
    geom_point()
  
  
  vp3 <- viewport(width=0.5, height=0.5, x=0.25, y=0.25)
  pairs <- data.frame(t(sapply(1:1000, function(foo) randomCorr(0))))
  names(pairs) <- c("Z1", "Z2")
  wyk3 <- qplot(x=Z1, y=Z2, data=pairs) +
    geom_point()
  
  
  vp4 <- viewport(width=0.5, height=0.5, x=0.75, y=0.25)
  pairs <- data.frame(t(sapply(1:1000, function(foo) randomCorr(0.4))))
  names(pairs) <- c("Z1", "Z2")
  wyk4 <- qplot(x=Z1, y=Z2, data=pairs) +
    geom_point()
  
  vp <- viewport()
  print(wyk1, vp=vp1)
  print(wyk2, vp=vp2)
  print(wyk3, vp=vp3)
  print(wyk4, vp=vp4)
}
