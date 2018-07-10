	protected void releaseConnection(HttpClient client){

		try{

			GetMethod getMehod = new GetMethod("/member/logout.jsp");

			getMehod.setRequestHeader("Keep-Alive", "300");

			getMehod.setRequestHeader("Connection", "keep-alive");

			

			//ログアウト

			client.executeMethod(getMehod);

		}

		catch(IOException exception){

			exception.printStackTrace();

		}

	}
