package com.tealduck.game.component;


import com.tealduck.game.Team;
import com.tealduck.game.engine.Component;


/**
 *
 */
public class TeamComponent extends Component {
	public Team team;


	/**
	 * @param team
	 */
	public TeamComponent(Team team) {
		this.team = team;
	}


	@Override
	public String toString() {
		return "TeamComponent(" + team.toString() + ")";
	}
}
