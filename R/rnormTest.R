
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

# boxMuller <- function(n)
# {
#   U1 <- runif(n)
#   U2 <- runif(n)
#   roots <- sqrt( -2 * log(U1) )
#   N1 <- roots * cos(2 * pi * U2)
#   N2 <- roots * sin(2 * pi * U2)
#   c(N1, N2)
# }
# 
# polarRejection <- function(n)
# {
#   V1 <- 2*runif(1.5*n) - 1
#   V2 <- 2*runif(1.5*n) - 1
#   W <- V1^2 + V2^2
#   hit <- which(W <= 1)
#   V1 <- (V1[hit])[1:n]
#   V2 <- (V2[hit])[1:n]
#   W <- (W[hit])[1:n]
#   root <- sqrt( -2 * log(W) / W )
#   N1 <- root * V1
#   N2 <- root * V2
#   c(N1, N2)
# }
# 
# testNormalGenerators <- function(n)
# {
#   bm <- system.time( boxMuller(n) )
#   pr <- system.time( polarRejection(n) )
#   print(paste("Box-Muller:", round(bm[1], 2), "seconds."))
#   print(paste("Polar rejection method:", round(pr[1], 2), "seconds."))
# }

