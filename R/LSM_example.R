M <- matrix(nrow=10, ncol=5)
M[1,] <- c(1, 98, 108, 102, 112)
M[2,] <- c(2, 98, 98, 95, 105)
M[3,] <- c(3, 98, 102, 97, 93)
M[4,] <- c(4, 98, 105, 98, 106)
M[5,] <- c(5, 98, 92, 102, 98)
M[6,] <- c(6, 98, 107, 103, 111)
M[7,] <- c(7, 98, 108, 113, 118)
M[8,] <- c(8, 98, 96, 99, 109)
M[9,] <- c(9, 98, 107, 98, 96)
M[10,] <- c(10, 98, 97, 103, 97)

pairmax <- function(v, w)
{
  l <- min(length(w), length(v))
  if (l == 0)
  {
    if (length(w) > 0) return(w)
    else return(v)
  }
  res <- sapply(1:l, function(i) max(v[i],w[i]))
  if (l < length(w)) return(c(res, w[(l+1):length(w)]))
  else if (l < length(v)) return(c(res, v[(l+1):length(v)]))
  else return(res)
}

r <- 0.0606

cashflow_t <- rep(1, 10)
cashflow_v <- pairmax(rep(0,10), 100-M[,5])
print( cbind(cashflow_t, cashflow_v) )

onestep <- function(k)
{
  colnr <- k+2 #kolumna z kursem w czasie t_k 
  nr <- which(M[,colnr] < 100)
  X <- M[nr,colnr]
  X2 <- X^2
  Y <- cashflow_v[nr] #niedyskontowany przeplyw
  disc <- exp(- r * (cashflow_t[nr] - k/3))
  Yd <- Y*disc
  data <- data.frame(nr, X, X2, Y, disc, Yd)
  print( data )
  lmres <- lm(Yd~X+X2, data=data)
  a <- lmres$coefficients[3]
  b <- lmres$coefficients[2]
  c <- lmres$coefficients[1]
  print( paste(a,"X^2 +", b, "X +", c) )
  len <- length(nr)
  Now <- pairmax(rep(0,len), 100-M[nr,colnr])
  Future <- sapply(1:len, function(i) a*X2[i] + b*X[i] + c)
  print( cbind(nr, Now, Future) )
  exnr <- nr[which(Now > Future)]
  cashflow_t[exnr] <<- k/3
  cashflow_v[exnr] <<- Now[which(Now > Future)]
  print( cbind(cashflow_t, cashflow_v) )
}

onestep(2)
onestep(1)

print( paste("price =",
             mean( exp(-r*cashflow_t) * cashflow_v )
))
