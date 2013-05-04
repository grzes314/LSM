source("MonteCarloComparision.R")

################################################################################
################ Below is code testing Monte Carlo methods #####################
############## We will calculate \int_0^1 e^x dx by simulation #################
################################################################################


# Creates functions which will perform random sampling.
# result -- list with fields
#   cmcFun -- drawing function for crude Monte Carlo
#   avFun -- drawing function for antithetic variates
#   cvFun -- drawing function for control variates
creatingDrawingFuns <- function()
{
  cmcFun <- function(n)
  {
    exp( runif(n) )
  }
  avFun <- function(n)
  {
    U <- runif(n)
    list (
      fst = exp(U),
      snd = exp(1-U)
    ) 
  }
  cvFun <- function(n)
  {
    U <- runif(n)
    list (
      Y = exp(U),
      X = U
    )
  }
  list( cmcFun = cmcFun,
        avFun = avFun,
        cvFun = cvFun
  )
}

# Function calculates \int_0^1 e^x dx by simulation using several MC methods.
# ns -- vectors with numbers of simulation. For crude MC two times as much
#    -- simulations will be perfomered.
# return -- data frame is result. One row is for n from ns. Columns are
#   estimated expect values and standard errors for all methods.
compareMC_integral <- function(ns, toFile=FALSE)
{
  F <- creatingDrawingFuns()
  compareMC(ns, F$cmcFun, F$avFun, F$cvFun, 0.5, toFile)
}

# For every method K times runs estimation with n replications (in case of CMC
# 2n replications).
# K -- how many times run estimation.
# n -- how many repliations in one estimation
boxesMC_integral <- function(K=100, n=10000)
{
  F <- creatingDrawingFuns()
  boxesMC(K, n, F$cmcFun, F$avFun, F$cvFun, 0.5)
}

convergence_integral <- function(n=10000)
{
  F <- creatingDrawingFuns()
  convergence(n, F$cmcFun, F$avFun, F$cvFun, 0.5, exp(1)-1)
}
