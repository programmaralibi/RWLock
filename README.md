RWLock
======

Requirement :
Implement a version of the CDLList that is protected by your own R/W locks in place of fine grain locks and the coarse grain lock.

(1) Note, you must first implement a R/W lock which adheres to the following semantics:
    (a) In the implementation of RW lock, you must define four methods: lockRead, unlockRead, lockWrite, unlockWrite.   
    These methods should NOT be synchronized.
    (b) Any number of readers may hold a R/W lock, but only one writer may hold the lock.
    (c) Readers and writers may not hold the lock at the same time.
    (d) The locks must preserve a notion of “fairness.”

(2) We will define “fairness” as follows: when many readers hold a lock and a writer attempts to acquire it, no further readers will acquire the lock until that writer is allowed to proceed. The writer, however, still must wait until the current readers, which hold the lock are done and release the lock.  Once the lock is released writers and readers have equal chance to acquire the lock.  This means you do not need to guarantee that a writer acquires the locks, just that it has a chance to acquire the lock.

(3) For this assignment, assume that Cursor objects will never be shared between different threads. This means you do not need to build in concurrency control into the cursors.

(4) The locks do not need to be re-entrant and you may read from an object if you have acquired its write lock.

(5)  We want to build our lock out of Compare And Set (CAS) operations instead of using synchronized blocks or methods.  This means you will need to encode the lock states as an Atomic Integer, provided in the Java.util.concurrent package (java.util.concurrent.atomic.AtomicInteger). Remember that we typically perform CAS operations inside a loop until the CAS operation returns the result we expected.  You will be using CAS to encode the state transitions we discussed in class.
You should encode the lock states as follows:
0 locked for write
1 unlocked
-1 unlocked, but giving lock to writer
all other numbers locked for read
positive number N>1  --- indicates locked for read, N-1 = number of readers
negative number N<-1 --- indicates locked for read, but writers are enqueued

 Thus, |N|-1 = number of readers current holding the lock

(6) Create an object that will serve as a lock. Use this object ONLY to suspend and notify threads inside your lock. You should use the atomic integer in step (5) to guarantee your lock has no data races. You can use this lock to provide synchronization for your queue of suspended threads.
