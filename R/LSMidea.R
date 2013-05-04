trajectory <- function(K, r = 0.05, vol=0.2, S0 = 100, T = 1.0, N = NULL)
{    
  dt <- T/K
  S <- rep(0, K+1)
  if (is.null(N))
    N <- rnorm(K)
  S[1] <- S0
  for (j in seq(from=2, to=K+1, by=1))
  {
    S[j] = S[j-1]*exp((r - 0.5*vol^2)*dt + N[j-1]*vol*sqrt(dt))
  }   
  S
}

LSMidea1 <- function(r = 0.05, vol=0.3, S0 = 100, N = NULL)
{
  K <- 250
  K1 <- 200
  S1 <- trajectory(K, r, vol, S0, T=1, N=N)
  res <- sapply(1:6, function(foo) trajectory(K-K1, r, vol, S1[K1], T=0.2))
  
  plot((1:K1)/K, S1[1:K1], xlim=c(0,1), ylim=c(0,1.8*S0),
       xlab="time", ylab="stock", type='l', lwd=3)
  for (i in 1:6)
  {
    lines((K1:(K-1))/K, res[,i])
  }
  abline(v=0.8)
  abline(h=S1[K1])
  points(0.8, S1[K1], bg="black", pch=21)
}

LSMidea2 <- function(r = 0.05, vol=0.3, S0 = 100, N = NULL)
{
  colors <- c("red", "blue", "green", "orange", "violet")
  K <- 250
  K1 <- 200
  S1 <- trajectory(K, r, vol, S0, T=1, N=N)
  plot((1:K1)/K, S1[1:K1], xlim=c(0,1), ylim=c(0,1.8*S0),
       xlab="time", ylab="stock", type='l', lwd=3)
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

