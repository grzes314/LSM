
################################################################################
################## Functions for testing random generators #####################
################################################################################

boxMuller <- function()
{
  U <- runif(2)
  root <- sqrt( -2 * log(U[1]) )
  N1 <- root * cos(2 * pi * U[2])
  N2 <- root * sin(2 * pi * U[2])
  list(fst = N1, snd = N2)
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
