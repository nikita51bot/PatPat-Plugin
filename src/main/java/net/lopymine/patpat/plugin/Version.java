package net.lopymine.patpat.plugin; // TODO переместить в более правильное место

import org.jetbrains.annotations.NotNull;

public record Version(int major, int minor, int patch) {

	public static final Version SERVER_CONFIG_VERSION = Version.of("1.0.0");

	public static final Version PACKET_V1_VERSION = Version.of("1.0.0");
	public static final Version PACKET_V2_VERSION = Version.of("1.2.0");

	public static Version of(@NotNull String version) {
		String[] numbers = version.split("\\.");
		int major = 0;
		int minor = 0;
		int patch = 0;
		try {
			major = Integer.parseInt(numbers[0]);
			minor = Integer.parseInt(numbers[1]);
			patch = Integer.parseInt(numbers[2]);
		} catch (Exception ignored) {
			// Use default values
		}
		return new Version(major, minor, patch);
	}

	public boolean isLessThan(Version version) {
		if (this.major != version.major) {
			return this.major < version.major;
		}
		if (this.minor != version.minor) {
			return this.minor < version.minor;
		}
		if (this.patch != version.patch) {
			return this.patch < version.patch;
		}
		return false;
	}

	public boolean isMoreThan(Version version) {
		if (this.major != version.major) {
			return this.major > version.major;
		}
		if (this.minor != version.minor) {
			return this.minor > version.minor;
		}
		if (this.patch != version.patch) {
			return this.patch > version.patch;
		}
		return false;
	}

	public boolean isGreaterOrEqualThan(Version version) {
		return !this.isLessThan(version);
	}

	public boolean isLesserOrEqualThan(Version version) {
		return !this.isMoreThan(version);
	}

	public boolean is(Version version) {
		return this.major == version.major &&
				this.minor == version.minor &&
				this.patch == version.patch;
	}

	@Override
	public String toString() {
		return "%d.%d.%d".formatted(this.major, this.minor, this.patch);
	}
}
