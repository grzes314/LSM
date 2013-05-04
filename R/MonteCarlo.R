
################################################################################
#################### Functions for Monte Carlo estimation ######################
################################################################################

# Cumulative average
cumav <- function(vec)
{
  n <- length(vec)
  cumsum(vec) / (1:n);
}

# Typical result of estimating function is a list with following fields:
# est -- estimated value
# sd -- standard deviation
# se -- standard error

# Function calculates several values for vector of samples
# Y -- vector of samples.
# return -- list of values described above.
samples <- function(Y)
{
  n <- length(Y)
  av <- mean(Y)
  stddev <- sd(Y)
  se <- stddev / sqrt(n)
  list ( est = av,
         cumav = cumav(Y),
         sd = stddev,
         se = se
  )  
}

# Function returns value of crude Monte Carlo estimator for given parameters.
# n -- number of simulations.
# rfun -- function for generating random values, it shoud take as an argument
#   natural number k and return a vector of k independent random values.
# return -- result of estimation described above.
crudeMC <- function(n, rfun, seed=NULL)
{
  if (!is.null(seed))
    set.seed(seed)
  Y <- rfun(n)
  samples(Y)
}

# Function returns value of antithetic Monte Carlo method for given parameters.
# n -- number of simulations.
# rfun -- function for generating random values, it shoud take as an argument
#   natural number k and return a list with fields fst and snd. Each field is a
#   vector of k independent random values.
# return -- result of estimation described above.
antitheticVariates <- function(n, rfun, seed=NULL)
{
  if (!is.null(seed))
    set.seed(seed)
  Y <- rfun(n)
  Y <- ( Y$fst + Y$snd ) / 2
  samples(Y)
}

# Function returns value of control variates Monte Carlo estimator for given
# parameters.
# n -- number of simulations.
# rfun -- function for generating random values, it shoud take as an argument
#   natural number k and return a list with two fields: X and Y. Each field is a
#   vector of k independent random values.
# EX -- expected value of X
# return -- result of estimation described above.
controlVariates <- function(n, rfun, EX, seed=NULL)
{
  if (!is.null(seed))
    set.seed(seed)
  res <- rfun(n)
  Y <- res$Y
  X <- res$X
  c <- -cov(X,Y)/var(X)
  Z <- Y + c*(X - EX)
  samples(Z)
}
