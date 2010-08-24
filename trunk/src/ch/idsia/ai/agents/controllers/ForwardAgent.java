package ch.idsia.ai.agents.controllers;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 8, 2009
 * Time: 4:03:46 AM
 * Package: ch.idsia.controllers.agents.controllers;
 */
public class ForwardAgent extends BasicAIAgent implements Agent
{
    int trueJumpCounter = 0;
    int trueSpeedCounter = 0;

    public ForwardAgent()
    {
        super("ForwardAgent");
        reset();
    }

    public void reset()
    {
        action = new boolean[Environment.numberOfButtons];
        action[Mario.KEY_RIGHT] = true;
        action[Mario.KEY_SPEED] = true;
        trueJumpCounter = 0;
        trueSpeedCounter = 0;
    }

    private boolean DangerOfGap(byte[][] levelScene)
    {
        for (int x = 7; x < 19; ++x)
        {
            boolean f = true;
            for(int y = 9; y < 19; ++y)
            {
                if  (levelScene[y][x] != 0)
                    f = false;
            }
            if (f || levelScene[10][9] == 0 || (marioState[1] > 0 && (levelScene[10][8] != 0 || levelScene[10][9] != 0)) )
                return true;
        }
        return false;
    }

    private boolean DangerOfGap()
    {
        return DangerOfGap(levelScene);
    }

    public boolean[] getAction()
    {
        // this Agent requires observation integrated in advance.

        if (mergedObservation[9][11] != 0 || mergedObservation[9][10] != 0 ||  DangerOfGap())
        {
            if (isMarioAbleToJump || ( !isMarioOnGround && action[Mario.KEY_JUMP]))
            {
                action[Mario.KEY_JUMP] = true;
            }
            ++trueJumpCounter;
        }
        else
        {
            action[Mario.KEY_JUMP] = false;
            trueJumpCounter = 0;
        }

        if (trueJumpCounter > 16)
        {
            trueJumpCounter = 0;
            action[Mario.KEY_JUMP] = false;
        }

        action[Mario.KEY_SPEED] = DangerOfGap();
        return action;
    }
}
