package bg.sofia.uni.fmi.mjt.battleshipsonline;

import java.io.PrintWriter;

public class WaterField implements Field {
	private Status status;

	public WaterField() {
		status = Status.NO_HIT;
	}

	@Override
	public char getIcon(boolean isEnemy) {
		if (status.equals(Status.NO_HIT)) {
			return Constants.NO_HIT_ICON;
		} else {
			return Constants.HIT_EMPTY_FIELD_ICON;
		}
	}

	@Override
	public boolean shootAt(PrintWriter pw) {
		switch (status) {
		case NO_HIT:
			pw.println("You have missed!");
			status = Status.HIT_WATER;
			break;
		case HIT_WATER:
			pw.println("This field is already hit!");
			return false;
		default:
			break;
		}
		return true;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public String getType() {
		return Constants.FIELD_TYPE_WATER;
	}

}
