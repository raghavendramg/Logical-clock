# Logical-clock

Suppose the logical clock on each machine represents the number of messages have been sent
and received by this machine. It is actually a counter used by the process (or the machine
emulator) to count events. Randomly initialize the logical clock of individual processes and use
Berkeley’s algorithm to synchronize these clocks to the average clock. Select any process
as the time daemon to initiate the clock synchronization. After the synchronization, each process
prints out its logical clock to check the result of synchronization.
