# TaskScheduling
Project about task scheduling

Basics:
Flowshop scheduling - Operation nr 1 always on I machine, operation nr 2 always on II machine.
Jobshop scheduling - Instance generator assigns operations to machines for each task. In contrast to Flowshop we can have operation nr 2
on I machine and operation nr 1 on II machine.
Every task included two operations.
Operations of each task must be scheduled on different machines.
Operation1 of given task must be finished before Operation2 can start.

My problem:
Number of machines = 2.
Number of tasks = n.
Jobshop.
Operations can be interrupted (for example by maintenance), and then they are resumed with additional +25% time.
For first and second machine we have k maintenance with random time of start and random duration (specified by Instance Generator)
k>=n/10.
Minimization the total time of all operations.
