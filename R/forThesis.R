
library("rgl")
library("fields")
library("reshape")
library("ggplot2")

makeChart3D <- function(xs, ys, zs, xlab, ylab, zlab)
{
  val_lim <- range(zs)
  vlen <- ceiling(val_lim[2] - val_lim[1] + 1)
  colorlut <- topo.colors(vlen)
  col <- colorlut[ zs - val_lim[1]+1 ]
  #open3d()
  #persp3d(xs, ys, zs, color=col, axes=FALSE, back="lines", xlab = xlab, ylab = ylab, zlab = "")
  #axes3d( edges=c("x--", "y--") )
  drape.plot(xs, ys, zs, r = 1, xlab = xlab, ylab = ylab, zlab = zlab,
             theta=-30, phi=30, ticktype="detailed", horizontal=FALSE)
  #image.plot( legend.only=TRUE, zlim= c(min(zs), max(zs)), nlevel=vlen,  col=colorlut)
}


fmt <- function(digits)
{
  function(x) format(x, nsmall = digits,scientific = FALSE)
}

my_theme <- function(base_size = 14, base_family = "")
{
  theme_bw(base_size = base_size, base_family = base_family) %+replace%
    theme(
      panel.grid.minor.y = element_line(size=3),
      panel.grid.major = element_line(colour = "grey")
    )
}

removeUnderscoreFromNames <- function(names)
{
  sapply(names, function(name) gsub("_", " ", name))  
}

# first column of frame is x values, the rest are y values.
makeChart2D <- function(frame, xLab, yLab)
{
  data_long <- melt(frame, id=1)  # convert to long format
  names(data_long) <- c("xVal", "variable", "value")
  legnames <- removeUnderscoreFromNames(colnames(frame)[-1])
  
  plot <- ggplot(data=data_long, aes(x=xVal, y=value, colour=variable)) +
    geom_line() + xlab(xLab)  + ylab(yLab) +
    my_theme(16) + theme(legend.justification=c(1,1), legend.position=c(1,1),
                       legend.text = element_text(colour="black", size = 16, face = "plain")) +
    guides(color=guide_legend(title=NULL)) +
    scale_colour_discrete(labels = legnames) +
    scale_y_continuous(labels=fmt(2))
  plot
}



