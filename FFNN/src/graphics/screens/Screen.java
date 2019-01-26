package graphics.screens;

public class Screen {

	public static final int MAIN_MENU = 0;
	public static final int FEED_FORWARD = 1;
	public static final int CONVOLUTIONAL = 2;

	public static Screen createScreen(int type) {
		switch (type) {
			case 0:
				return new MainMenuScreen();
			
			case 1:
				return new FeedForwardScreen();

			case 2:
				return new ConvolutionalScreen();
		}
		
		return null;
	}
}
