package net.lopymine.patpat.plugin.config;

import com.google.gson.annotations.JsonAdapter;
import lombok.Getter;
import lombok.Setter;

import net.lopymine.patpat.plugin.command.ratelimit.Time;
import net.lopymine.patpat.plugin.config.adapter.TimeAdapter;

@Getter
@Setter
public class RateLimitConfig {

	private boolean enabled;
	private int tokenLimit;
	private int tokenIncrement;
	@JsonAdapter(TimeAdapter.class)
	private Time tokenIncrementInterval;
	private String permissionBypass;

}
