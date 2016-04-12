/**
 * VMSessionManagerImplPortBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.vmsessionmanager;

public class VMSessionManagerImplPortBindingStub extends org.apache.axis.client.Stub implements eduze.vms.facilitator.logic.mpi.vmsessionmanager.VMSessionManager {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[2];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("connect");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "ConnectionResult"));
        oper.setReturnClass(eduze.vms.facilitator.logic.mpi.vmsessionmanager.ConnectionResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "UnknownFacilitatorException"),
                      "eduze.vms.facilitator.logic.mpi.vmsessionmanager.UnknownFacilitatorException",
                      new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "UnknownFacilitatorException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "FacilitatorAlreadyConnectedException"),
                      "eduze.vms.facilitator.logic.mpi.vmsessionmanager.FacilitatorAlreadyConnectedException",
                      new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "FacilitatorAlreadyConnectedException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "MeetingAlreadyStartedException"),
                      "eduze.vms.facilitator.logic.mpi.vmsessionmanager.MeetingAlreadyStartedException",
                      new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "MeetingAlreadyStartedException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "ServerNotReadyException"),
                      "eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException",
                      new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "ServerNotReadyException"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getStatus");
        oper.setReturnType(new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "sessionStatus"));
        oper.setReturnClass(eduze.vms.facilitator.logic.mpi.vmsessionmanager.SessionStatus.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

    }

    public VMSessionManagerImplPortBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public VMSessionManagerImplPortBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public VMSessionManagerImplPortBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.1");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "ConnectionResult");
            cachedSerQNames.add(qName);
            cls = eduze.vms.facilitator.logic.mpi.vmsessionmanager.ConnectionResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "FacilitatorAlreadyConnectedException");
            cachedSerQNames.add(qName);
            cls = eduze.vms.facilitator.logic.mpi.vmsessionmanager.FacilitatorAlreadyConnectedException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "MeetingAlreadyStartedException");
            cachedSerQNames.add(qName);
            cls = eduze.vms.facilitator.logic.mpi.vmsessionmanager.MeetingAlreadyStartedException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "ServerNotReadyException");
            cachedSerQNames.add(qName);
            cls = eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "sessionStatus");
            cachedSerQNames.add(qName);
            cls = eduze.vms.facilitator.logic.mpi.vmsessionmanager.SessionStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "UnknownFacilitatorException");
            cachedSerQNames.add(qName);
            cls = eduze.vms.facilitator.logic.mpi.vmsessionmanager.UnknownFacilitatorException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public eduze.vms.facilitator.logic.mpi.vmsessionmanager.ConnectionResult connect(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException, eduze.vms.facilitator.logic.mpi.vmsessionmanager.UnknownFacilitatorException, eduze.vms.facilitator.logic.mpi.vmsessionmanager.FacilitatorAlreadyConnectedException, eduze.vms.facilitator.logic.mpi.vmsessionmanager.MeetingAlreadyStartedException, eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "connect"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {arg0, arg1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (eduze.vms.facilitator.logic.mpi.vmsessionmanager.ConnectionResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (eduze.vms.facilitator.logic.mpi.vmsessionmanager.ConnectionResult) org.apache.axis.utils.JavaUtils.convert(_resp, eduze.vms.facilitator.logic.mpi.vmsessionmanager.ConnectionResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof eduze.vms.facilitator.logic.mpi.vmsessionmanager.UnknownFacilitatorException) {
              throw (eduze.vms.facilitator.logic.mpi.vmsessionmanager.UnknownFacilitatorException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof eduze.vms.facilitator.logic.mpi.vmsessionmanager.FacilitatorAlreadyConnectedException) {
              throw (eduze.vms.facilitator.logic.mpi.vmsessionmanager.FacilitatorAlreadyConnectedException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof eduze.vms.facilitator.logic.mpi.vmsessionmanager.MeetingAlreadyStartedException) {
              throw (eduze.vms.facilitator.logic.mpi.vmsessionmanager.MeetingAlreadyStartedException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException) {
              throw (eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public eduze.vms.facilitator.logic.mpi.vmsessionmanager.SessionStatus getStatus() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "getStatus"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (eduze.vms.facilitator.logic.mpi.vmsessionmanager.SessionStatus) _resp;
            } catch (java.lang.Exception _exception) {
                return (eduze.vms.facilitator.logic.mpi.vmsessionmanager.SessionStatus) org.apache.axis.utils.JavaUtils.convert(_resp, eduze.vms.facilitator.logic.mpi.vmsessionmanager.SessionStatus.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
