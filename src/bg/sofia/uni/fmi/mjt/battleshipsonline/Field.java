package bg.sofia.uni.fmi.mjt.battleshipsonline;

import java.io.PrintWriter;

public interface Field {
	public char getIcon(boolean isEnemy);
	public boolean shootAt(PrintWriter pw);
	public Status getStatus();
	public String getType();
}
