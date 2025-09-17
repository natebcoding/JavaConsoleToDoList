public class Timer_Helper implements Runnable {
    private final int secondsToCount;

    public Timer_Helper(int secondsToCount) {
        this.secondsToCount = secondsToCount;
    }

    @Override
    public void run() {
        for (int i = secondsToCount; i >= 1; i--) {
            System.out.print("\rReturning to main menu in " + i + " ");
            System.out.flush();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted. ");
                return;
            }
        }
        System.out.print("\r                             \r");
    }
}