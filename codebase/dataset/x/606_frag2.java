    public NotificationChain basicSetCallerId(DynamicValue newCallerId, NotificationChain msgs) {

        DynamicValue oldCallerId = callerId;

        callerId = newCallerId;

        if (eNotificationRequired()) {

            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionstepPackage.SET_CALLER_ID__CALLER_ID, oldCallerId, newCallerId);

            if (msgs == null) msgs = notification; else msgs.add(notification);

        }

        return msgs;

    }



    /**

	 * <!-- begin-user-doc -->

   * <!-- end-user-doc -->

	 * @generated

	 */

    public void setCallerId(DynamicValue newCallerId) {

        if (newCallerId != callerId) {

            NotificationChain msgs = null;

            if (callerId != null) msgs = ((InternalEObject) callerId).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.SET_CALLER_ID__CALLER_ID, null, msgs);

            if (newCallerId != null) msgs = ((InternalEObject) newCallerId).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.SET_CALLER_ID__CALLER_ID, null, msgs);

            msgs = basicSetCallerId(newCallerId, msgs);

            if (msgs != null) msgs.dispatch();

        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.SET_CALLER_ID__CALLER_ID, newCallerId, newCallerId));

    }



    /**

	 * <!-- begin-user-doc -->

   * <!-- end-user-doc -->

	 * @generated

	 */

    @Override

    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
