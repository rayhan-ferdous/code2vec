        super(auctionName, AuctionTags.CONTINUOUS_DOUBLE_AUCTION, durationOfAuction);

        compAsks = new OrderAsksByPriceAsc();

        compBids = new OrderBidsByPriceDesc();

        asks = new LinkedList();

        bids = new LinkedList();

    }



    /**

	 * This method is called when the auction is started

	 */

    public void onStart() {

        MessageCallForBids msg = new MessageCallForBids(super.getAuctionID(), super.getAuctionProtocol(), 0D, 1);
