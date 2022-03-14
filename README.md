# PID-cruise-controller
> PID and 'bang-bang' speed controllers for vehicles

This program uses a speed simulator that gives an output speed and can receive acceleration inputs, but without the possibility to set an exact speed.

<h3>What I learned</h3>
<ul>
  <li>Implementing mathematical functions as a discrete data-set and real-time mapping, i.e. taking speed records, calculate error margin with the objective and compute an acceleration pulse, based on error coefficient, its derivative and previous errors integration.</li>
  <li>Observer pattern implementation</li>
</ul>

![Capture](https://user-images.githubusercontent.com/29238761/158176752-b0fc9405-8c7c-4ced-a3d5-796aadd314af.png)
