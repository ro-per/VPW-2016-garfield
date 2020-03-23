import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static char[][] buurt;
	public static Coordinaat huis;
	public static List<Coordinaat> voedsel;
	public static int hoeveelheidEten, uitkomst;
	public static Coordinaat temp;
	public static long start, stop, totaal;

	public static void main(String[] args) {
		// VARIABLES
		int n, tijd, h, b, xCoo, yCoo;
		String[] invoer;

		// AANTAL TESTGEVALLEN
		Scanner sc = new Scanner(System.in);
		n = Integer.valueOf(sc.nextLine());
		start = System.nanoTime();
		for (int x = 0; x < n; x++) {
			// INVOER
			invoer = sc.nextLine().split(" ");
			b = Integer.valueOf(invoer[0]); // breedte = kolommen
			h = Integer.valueOf(invoer[1]); // hoogte = rijen
			tijd = Integer.valueOf(invoer[2]); // tijd
			// VARIABLES
			buurt = new char[h][b];

			// LEES DE BUURT IN
			for (int y = 0; y < h; y++) {
				buurt[y] = sc.nextLine().toCharArray();
			}

			// ZOEK GARFIELD
			for (yCoo = 0; yCoo < h; yCoo++) {
				for (xCoo = 0; xCoo < b; xCoo++) {
					if (buurt[yCoo][xCoo] == 'G') {
						huis = new Coordinaat(xCoo, yCoo);
					}
				}
			}
			// SLA AL HET ETEN OP
			voedsel = new ArrayList<Coordinaat>();
			for (yCoo = 0; yCoo < h; yCoo++) {
				for (xCoo = 0; xCoo < b; xCoo++) {
					if (buurt[yCoo][xCoo] == 'E') {
						temp = new Coordinaat(xCoo, yCoo, huis);
						voedsel.add(temp);
					}
				}
			}
			hoeveelheidEten = voedsel.size();
			uitkomst = doorZoekBuurt(tijd, huis);
			System.out.println(x + 1 + " " + uitkomst);
		}
		sc.close();
		stop = System.nanoTime();
		//System.out.println(stop-start);
	}

	public static int doorZoekBuurt(int tijd, Coordinaat vorige) {
		int tijdGa, tijdTerug;
		int tempTeller = 0;
		int etenTeller = 0;

		if (hoeveelheidEten <= 0) {
			return 0;
		}
		if(etenTeller==voedsel.size()) {
			return etenTeller;
		}
		for (Coordinaat coo : voedsel) {
			if (!coo.getBezocht()) {
				tijdGa = vorige.berekenAfstand(coo);
				tijdTerug = coo.getAfstandTotHuis();
				if (tijd >= tijdGa + tijdTerug + 1) {
					// DO
					tempTeller = 1;
					hoeveelheidEten--;
					tijd -= (tijdGa + 1);
					coo.changeState(true);
					// RECUR
					tempTeller += doorZoekBuurt(tijd, coo);
					// UNDONE
					coo.changeState(false);
					hoeveelheidEten++;
					etenTeller = Math.max(etenTeller, tempTeller);
					tijd += (tijdGa + 1);
				}
			}
		}
		return etenTeller;
	}
}

class Coordinaat {
	public int x, y;
	public int afstandTotHuis;
	public boolean bezocht;

	// DEFAULT CONSTRUCTOR
	public Coordinaat() {
		this.x = -1;
		this.y = -1;
		this.afstandTotHuis = -1;
	}

	// NORMAL CONSTRUCTOR zonder afstand tot huis
	public Coordinaat(int xCoo, int yCoo) {
		this.x = xCoo;
		this.y = yCoo;
		this.afstandTotHuis = 0;
	}

	// NORMAL CONSTRUCTOR met afstand tot huis
	public Coordinaat(int xCoo, int yCoo, Coordinaat huis) {
		this.x = xCoo;
		this.y = yCoo;
		int verLen = Math.abs(yCoo - huis.getY());
		int horLen = Math.abs(xCoo - huis.getX());
		this.afstandTotHuis = verLen + horLen;
	}

	// COPY CONSTRUCTOR
	public Coordinaat(Coordinaat coo) {
		this.x = coo.getX();
		this.y = coo.getY();
		this.afstandTotHuis = coo.getAfstandTotHuis();
	}

	// GETTERS
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getAfstandTotHuis() {
		return afstandTotHuis;
	}

	public boolean getBezocht() {
		return bezocht;
	}

	// SETTERS
	public void changeState(boolean b) {
		this.bezocht = b;
	}

	// METHODES
	public int berekenAfstand(Coordinaat anderEten) {
		int verLen = Math.abs(y - anderEten.getY());
		int horLen = Math.abs(x - anderEten.getX());
		return verLen + horLen;
	}

	// TO STRING METHOD
	public String toString() {// overriding the toString() method
		return "[" + x + "|" + y + "]";
	}

}
