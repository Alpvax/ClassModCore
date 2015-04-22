package alpvax.classmodcore.api.powers;

enum EnumPowerType
{
	/**
	 * Does something instantaneously when triggered<br>
	 * (no duration)
	 */
	INSTANT,
	/**
	 * Is active until disabled<br>
	 * (has unlimited duration)
	 */
	TOGGLED,
	/**
	 * Is active for a limited time-frame<br>
	 * (has limited duration)
	 */
	TIMED,
	/**
	 * Cannot be triggered, either by the player or passively<br>
	 * (no cooldown)
	 */
	CONTINUOUS;
}
