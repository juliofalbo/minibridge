package com.mini.bridge.game;

import com.mini.bridge.game.models.Match;
import com.mini.bridge.game.models.Player;
import com.mini.bridge.game.models.Round;
import com.mini.bridge.game.models.RoundUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GameApplicationTests {

	@Test
	void contextLoads() {
		Player julio = new Player("Julio");
		Player mauricio = new Player("Mauricio");
		Player idalina = new Player("Idalina");
		Player fernanda = new Player("Fernanda");
		Player paloma = new Player("Paloma");
		Player marcela = new Player("Marcela");
		Player juliana = new Player("Juliana");
		Player gustavo = new Player("Gustavo");

		Match match = new Match();
		match.addPlayers(julio);
		match.addPlayers(mauricio);
		match.addPlayers(idalina);
		match.addPlayers(fernanda);
		match.addPlayers(paloma);
		match.addPlayers(marcela);
		match.addPlayers(juliana);
		match.addPlayers(gustavo);

		Round round = new Round(match, 1);

		RoundUser round0 = new RoundUser(julio, 1);
		round0.setNumberOfSetOfCardsWon(1);

		RoundUser round1 = new RoundUser(mauricio, 0);
		RoundUser round2 = new RoundUser(idalina, 0);
		RoundUser round3 = new RoundUser(fernanda, 0);
		RoundUser round4 = new RoundUser(paloma, 0);
		RoundUser round5 = new RoundUser(marcela, 0);
		RoundUser round6 = new RoundUser(juliana, 1);
		RoundUser round7 = new RoundUser(gustavo, 0);


		round.addUserRounds(round0);
		round.addUserRounds(round1);
		round.addUserRounds(round2);
		round.addUserRounds(round3);
		round.addUserRounds(round4);
		round.addUserRounds(round5);
		round.addUserRounds(round6);
		round.addUserRounds(round7);

		match.addRounds(round);

		System.out.println(match.partialScore());
	}

}
