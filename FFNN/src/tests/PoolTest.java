package tests;

import main.NN.Neurons.Pool;

public class PoolTest {
	
	private static Pool pool = new Pool();

	public static void main(String[] args) {
		pool.setSize(4, 4);
		
		for (int i = 0; i < 4; i++) {
			for (int n = 0; n < 4; n++) {
				pool.setElement((int) (Math.random() * 10), i, n);
			}
		}
		
		System.out.println(pool);
		pool.downSample();
		System.out.println(pool);
	}

}
