    public NotificationChain basicSetContext(DynamicValue newContext, NotificationChain msgs) {

        DynamicValue oldContext = context;

        context = newContext;

        if (eNotificationRequired()) {

            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionstepPackage.SET_CONTEXT__CONTEXT, oldContext, newContext);

            if (msgs == null) msgs = notification; else msgs.add(notification);

        }

        return msgs;

    }



    /**

	 * <!-- begin-user-doc -->

   * <!-- end-user-doc -->

	 * @generated

	 */

    public void setContext(DynamicValue newContext) {

        if (newContext != context) {

            NotificationChain msgs = null;

            if (context != null) msgs = ((InternalEObject) context).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.SET_CONTEXT__CONTEXT, null, msgs);

            if (newContext != null) msgs = ((InternalEObject) newContext).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.SET_CONTEXT__CONTEXT, null, msgs);

            msgs = basicSetContext(newContext, msgs);

            if (msgs != null) msgs.dispatch();

        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.SET_CONTEXT__CONTEXT, newContext, newContext));

    }



    /**

	 * <!-- begin-user-doc -->

   * <!-- end-user-doc -->

	 * @generated

	 */

    @Override

    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
