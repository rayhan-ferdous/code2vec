import com.sap.aii.af.service.administration.api.monitoring.ProcessState;

import com.sap.engine.interfaces.messaging.api.Message;



public class ChannelMonitoringSapImpl implements ChannelMonitoring, Traceable {



    private BaseTracer baseTracer = null;



    private ProcessContextFactory.ParamSet ps;



    private MonitoringManager mm = null;
