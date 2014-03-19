import java.util.concurrent.atomic.AtomicInteger;

/*
 * Name : Rastogi, Amber
 * UBID : 50097978
 * Email : amberras@buffalo.edu
 */

/*
	1) Read Write Lock implemented using basic Compare and Set operation.
*/ 
 
 
public class RWLock {
	
	private AtomicInteger ai = new AtomicInteger(1);
	
	public void lockRead(){
		boolean lockAcquired = false;
		while(!lockAcquired) {
			int aiValue = ai.get();
			if(aiValue < 0) {
				synchronized (this) {
				try {
					this.wait(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			} else {
				lockAcquired = ai.compareAndSet(aiValue, aiValue+1);
			}
		}
	}

	public void unlockRead() {
		boolean lockReleased = false;
		while(!lockReleased) {
			int aiValue = ai.get();
			if(aiValue < -1) {
				lockReleased = ai.compareAndSet(aiValue, aiValue+1);
			} else if(aiValue > 1) {
				lockReleased = ai.compareAndSet(aiValue, aiValue-1);
			}
		}
		synchronized (this) {
			this.notifyAll();
		}
	}
		
	public void lockWrite() {
		boolean lockAcquired = false;
		while(!lockAcquired) {
			int aiValue = ai.get();
			if(aiValue == -1 || aiValue == 1) {
				lockAcquired = ai.compareAndSet(aiValue, 0);
			} else {
				if(aiValue > 1) {
					ai.compareAndSet(aiValue, aiValue*-1);
				}
				synchronized (this) {
					try {
						this.wait(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
		
	public void unlockWrite() {
		while(ai.compareAndSet(0, 1));
		synchronized (this) {
			this.notifyAll();
		}
	}
}
