
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
  m <- ceiling(n/2)
  U1 <- runif(m)
  U2 <- runif(m)
  roots <- sqrt( -2 * log(U1) )
  N1 <- roots * cos(2 * pi * U2)
  N2 <- roots * sin(2 * pi * U2)
  c(N1, N2)
}

polarRejection <- function(n)
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
