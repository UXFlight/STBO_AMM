package AgentCB;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import XPlaneConnect.XPlaneConnect;
import main.Main;
import main.XPConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ingescape.*;


public class JavaAgentCB implements IopListener, ServiceListener {
    private static Logger _logger = LoggerFactory.getLogger(JavaAgentCB.class);
    boolean firstSignal = false;
    boolean secondSignal = false;
    boolean thirdSignal = false;
    boolean fourthSignal = false;
    public Main globalMain;
    public XPConnect globalXPConnect;
    public JavaAgentCB(Main main, XPConnect xPConnect) {
    globalMain = main;
    globalXPConnect = xPConnect;
    }

    @Override
    public void handleIOP(Agent agent, Iop iop, String name, IopType type, Object value) {
        _logger.debug("I AM INSIDE HANDLEIOP **received input {} with type {} and value {}", name, type, value);
        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "lat"))
        {
            globalXPConnect.setLat((double)value);
        }
        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "lon"))
        {
            globalXPConnect.setLon((double)value);
        }

        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "height"))
        {
            //globalXPConnect.setheight((double)value);
        }

        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "pitch"))
        {
            //globalXPConnect.setPitch((double)value);
        }

        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "roll"))
        {
            //globalXPConnect.setRoll((double)value);
        }

        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "heading"))
        {
            globalXPConnect.setHeading((double)value);
        }

        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "elevator"))
        {
            //globalXPConnect.setElevator((double)value);
        }

        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "aileron"))
        {
            //globalXPConnect.setAileron((double)value);
        }

        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "rudder"))
        {
            //globalXPConnect.setRudder((double)value);
        }


        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "fuelQuantity"))
        {
            globalXPConnect.setFuelQuantity((double)value);
        }

        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "groundSpeed"))
        {
            globalXPConnect.setGroundSpeed((double)value);
        }
        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "brakingAction"))
        {
            globalXPConnect.setBrakingAction((double)value);
        }
        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "thrustCmd"))
        {
            globalXPConnect.setThrustCmd((double)value);
        }

        if(iop == Iop.IGS_INPUT_T && type == IopType.IGS_DOUBLE_T && Objects.equals(name, "simTimeSinceStart"))
        {
            globalXPConnect.setSimTimeSinceStart((double)value);
        }


        if (iop == Iop.IGS_INPUT_T && type == IopType.IGS_STRING_T) {
            String inputString = (String) value;
           // _logger.debug("**processing string input: {}", inputString);
            agent.outputSetString("myString1", inputString);
        }

        if (iop == Iop.IGS_INPUT_T && type == IopType.IGS_IMPULSION_T && Objects.equals(name, "dashed_lines")) {
            globalMain.cpdlcManager.step = 0;
            globalMain.cpdlcManager.drawNext();
        }

        if (iop == Iop.IGS_INPUT_T && type == IopType.IGS_IMPULSION_T && Objects.equals(name, "solid_lines")) {
            globalMain.cpdlcManager.step = 1;
            globalMain.cpdlcManager.drawNext();
        }

        if (iop == Iop.IGS_INPUT_T && type == IopType.IGS_IMPULSION_T && Objects.equals(name, "alert_lines")) {
            globalMain.cpdlcManager.step = 2;
            globalMain.cpdlcManager.drawNext();
        }

        if (iop == Iop.IGS_INPUT_T && type == IopType.IGS_IMPULSION_T && Objects.equals(name, "Reset")) {
            globalMain.cpdlcManager.resetFlightPlan();
        }
    }

    @Override
    public void handleCallToService(Agent agent, String senderAgentName, String senderAgentUUID,
                                    String serviceName, List<Object> arguments, String token) {
        //_logger.debug("**received service call from {} ({}): {} (with token {})", senderAgentName, senderAgentUUID, serviceName, arguments, token);
    }
}