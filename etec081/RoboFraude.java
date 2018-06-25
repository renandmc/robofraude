package etec081;

import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import java.awt.Color;

/**
 * Robofraude - a robot by CRV (Cesar, Renan e Vinicius)
 */
public class RoboFraude extends AdvancedRobot
{
	
	int contadorBusca; // Conta quanto tempo busca alvo
	double viraArma; // Quanto virar arma quando busca
	String alvo; // Nome do alvo
	
	/**
	 * run: Robofraude's default behavior
	 */
	public void run() {		
		// Definindo cores do robô
		setColors(Color.black, Color.black, Color.red); // corpo, arma, radar
		alvo = null; // Seta nulo o alvo para buscar
		setAdjustGunForRobotTurn(true); // Mantem arma parada
		viraArma = 10; // Inicializa em 10 angulo para virar arma
		// Repetição principal do robô
		while(true) {				
			// Busca alvo			
			turnGunRight(viraArma);			
			// Aumenta contador da busca
			contadorBusca++;
			// Se não acha alvo por 2 turnos, olha para esquerda			
			if (contadorBusca > 2) {
				viraArma = -10;
			}
			// Se não acha alvo por 4 turnos, olha para direita			
			if (contadorBusca > 4) {
				viraArma = 10;
			}
			// Se não acha alvo em 6 turnos, procura outro alvo			
			if (contadorBusca > 6) {
				alvo = null;
			}
		}
	}

	/**
	 * onScannedRobot: Quando vê outro robô mira e atira quando estiver perto
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Se não tem alvo, define como alvo
		if (alvo == null) {
			alvo = e.getName();
		}		
		// Zera contador de busca, achou alvo
		contadorBusca = 0;		
		// Se alvo está longe, chega perto
		if (e.getDistance() >= 100) {
			// Ajusta mira com posição do robo alvo
			viraArma = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
			// Vira arma para direita na mira
			setTurnGunRight(viraArma);
			// Vira robo para direita
			setTurnRight(e.getBearing());
			// Segue na direção do alvo
			setAhead(e.getDistance() - 100);
			return;
		} else {
			fire(1);
		}
		// Alvo perto
		// Ajusta mira
		viraArma = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		// Vira arma para posição
		turnGunRight(viraArma);
		// Atira força 3
		fire(3);
		scan();
	}
			
	/**
	 * onHitRobot: Quando bate em um robô define como alvo
	 */
	public void onHitRobot(HitRobotEvent e) {	
		// Pega como alvo
		alvo = e.getName();		
	}
				
	/**
	 * onWin: Quando ganha faz um movimento
	 */
	public void onWin(WinEvent e) {
		for (int i = 0; i < 50; i++) {	
			turnRight(30);
			turnLeft(30);			
		}
	}
	
}