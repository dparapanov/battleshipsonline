package bg.sofia.uni.fmi.mjt.battleshipsonline;

import java.io.PrintWriter;

public class ShipField implements Field {
	private Ship ship;
	private Status status;

	private void updateStatus() {
		if (ship.getLives() == 0) {
			status = Status.DESTROYED;
		}
	} 

	public ShipField(Ship ship) {
		this.ship = ship;
		this.status = Status.NO_HIT;
	}

	@Override
	public char getIcon(boolean isEnemy) {
		updateStatus();
		if (status.equals(Status.NO_HIT)) {
			if (isEnemy) {
				return Constants.NO_HIT_ICON;
			} else {
				return Constants.SHIP_FIELD_ICON;
			}
		} else {
			return Constants.HIT_SHIP_ICON;
		}
	}

	@Override
	public boolean shootAt(PrintWriter pw) {
		if (status.equals(Status.NO_HIT)) {
			ship.hit(pw);
			status = Status.HIT;
			return true;
		} else {
			pw.println("This field is already hit");
			return false;
		}

	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public String getType() {
		return Constants.FIELD_TYPE_SHIP;
	}
}
