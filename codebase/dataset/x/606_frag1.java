    public NotificationChain basicSetStateVar(DynamicValue newStateVar, NotificationChain msgs) {

        DynamicValue oldStateVar = stateVar;

        stateVar = newStateVar;

        if (eNotificationRequired()) {

            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionstepPackage.GET_CALL_INFO__STATE_VAR, oldStateVar, newStateVar);

            if (msgs == null) msgs = notification; else msgs.add(notification);

        }

        return msgs;

    }



    /**

	 * <!-- begin-user-doc --> <!-- end-user-doc -->

	 * @generated

	 */

    public void setStateVar(DynamicValue newStateVar) {

        if (newStateVar != stateVar) {

            NotificationChain msgs = null;

            if (stateVar != null) msgs = ((InternalEObject) stateVar).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.GET_CALL_INFO__STATE_VAR, null, msgs);

            if (newStateVar != null) msgs = ((InternalEObject) newStateVar).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.GET_CALL_INFO__STATE_VAR, null, msgs);

            msgs = basicSetStateVar(newStateVar, msgs);

            if (msgs != null) msgs.dispatch();

        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.GET_CALL_INFO__STATE_VAR, newStateVar, newStateVar));

    }



    /**

	 * <!-- begin-user-doc --> <!-- end-user-doc -->

	 * @generated

	 */

    public DynamicValue getUniqueIdVar() {

        return uniqueIdVar;

    }



    /**

	 * <!-- begin-user-doc --> <!-- end-user-doc -->

	 * @generated

	 */

    public NotificationChain basicSetUniqueIdVar(DynamicValue newUniqueIdVar, NotificationChain msgs) {
