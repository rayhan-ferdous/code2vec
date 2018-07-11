    public NotificationChain basicSetValue(DynamicValue newValue, NotificationChain msgs) {

        DynamicValue oldValue = value;

        value = newValue;

        if (eNotificationRequired()) {

            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionstepPackage.SET_GLOBAL_VARIABLE__VALUE, oldValue, newValue);

            if (msgs == null) msgs = notification; else msgs.add(notification);

        }

        return msgs;

    }



    /**

	 * <!-- begin-user-doc -->

   * <!-- end-user-doc -->

	 * @generated

	 */

    public void setValue(DynamicValue newValue) {

        if (newValue != value) {

            NotificationChain msgs = null;

            if (value != null) msgs = ((InternalEObject) value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.SET_GLOBAL_VARIABLE__VALUE, null, msgs);

            if (newValue != null) msgs = ((InternalEObject) newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.SET_GLOBAL_VARIABLE__VALUE, null, msgs);

            msgs = basicSetValue(newValue, msgs);

            if (msgs != null) msgs.dispatch();

        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.SET_GLOBAL_VARIABLE__VALUE, newValue, newValue));

    }



    /**

	 * <!-- begin-user-doc -->

   * <!-- end-user-doc -->

	 * @generated

	 */

    public String getVariable() {

        return variable;

    }



    /**

	 * <!-- begin-user-doc -->

   * <!-- end-user-doc -->

	 * @generated

	 */

    public void setVariable(String newVariable) {
