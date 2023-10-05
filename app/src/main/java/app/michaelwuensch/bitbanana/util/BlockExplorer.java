package app.michaelwuensch.bitbanana.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import app.michaelwuensch.bitbanana.R;

/**
 * This class allows showing details of On-Chain transactions and addresses using
 * 3rd Party services (Block Explorers)
 */
public class BlockExplorer {

    private String mUrl;
    private boolean mIsNetworkSupported;
    private Context mContext;


    /**
     * Shows transaction details in a browser window using the preferred block explorer.
     *
     * @param transactionID transaction to show
     * @param ctx
     */
    public void showTransaction(String transactionID, Context ctx) {
        if (Wallet.getInstance().getNetwork() == Wallet.Network.REGTEST) {
            new AlertDialog.Builder(ctx)
                    .setMessage(R.string.regtest_blockexplorer_unavailable)
                    .setCancelable(true)
                    .setPositiveButton(R.string.ok, (dialog, whichButton) -> {
                    }).show();
            return;
        }

        mContext = ctx;
        String explorer = PrefsUtil.getPrefs().getString("blockExplorer", "Mempool.space");
        boolean isMainnet = Wallet.getInstance().getNetwork() == Wallet.Network.MAINNET;
        String networkID = "";
        mUrl = "";
        mIsNetworkSupported = true;

        switch (explorer) {
            case "BlockCypher":
                networkID = isMainnet ? "btc" : "btc-testnet";
                mUrl = "https://live.blockcypher.com/" + networkID + "/tx/" + transactionID;
                break;
            case "Blockstream":
                networkID = isMainnet ? "" : "testnet/";
                mUrl = "https://blockstream.info/" + networkID + "tx/" + transactionID;
                break;
            case "Blockstream (v3 Tor)":
                networkID = isMainnet ? "" : "testnet/";
                mUrl = "http://explorerzydxu5ecjrkwceayqybizmpjjznk5izmitf2modhcusuqlid.onion/" + networkID + "tx/" + transactionID;
                break;
            case "Mempool.space":
                mIsNetworkSupported = isMainnet;
                mUrl = "https://mempool.space/tx/" + transactionID;
                break;
            case "Blockchain Reader (Yogh)":
                mIsNetworkSupported = isMainnet;
                mUrl = "https://yogh.io/#tx:id:" + transactionID;
                break;
            case "OXT":
                mIsNetworkSupported = isMainnet;
                mUrl = "https://oxt.me/transaction/" + transactionID;
                break;
        }

        if (PrefsUtil.isTorEnabled()) {
            // Ask user to confirm risking privacy issues
            new UserGuardian(mContext, this::startBlockExplorer).privacyExternalLink();
        } else {
            startBlockExplorer();
        }
    }

    /**
     * Shows address details in a browser window using the preferred block explorer.
     *
     * @param address address to show
     * @param ctx
     */
    public void showAddress(String address, Context ctx) {

        if (Wallet.getInstance().getNetwork() == Wallet.Network.REGTEST) {
            new AlertDialog.Builder(ctx)
                    .setMessage(R.string.regtest_blockexplorer_unavailable)
                    .setCancelable(true)
                    .setPositiveButton(R.string.ok, (dialog, whichButton) -> {
                    }).show();
            return;
        }

        mContext = ctx;
        String explorer = PrefsUtil.getPrefs().getString("blockExplorer", "Mempool.space");
        boolean isMainnet = Wallet.getInstance().getNetwork() == Wallet.Network.MAINNET;
        String networkID = "";
        mUrl = "";
        mIsNetworkSupported = true;

        switch (explorer) {
            case "BlockCypher":
                networkID = isMainnet ? "btc" : "btc-testnet";
                mUrl = "https://live.blockcypher.com/" + networkID + "/address/" + address;
                break;
            case "Blockstream":
                networkID = isMainnet ? "" : "testnet/";
                mUrl = "https://blockstream.info/" + networkID + "address/" + address;
                break;
            case "Blockstream (v3 Tor)":
                networkID = isMainnet ? "" : "testnet/";
                mUrl = "http://explorerzydxu5ecjrkwceayqybizmpjjznk5izmitf2modhcusuqlid.onion/" + networkID + "address/" + address;
                break;
            case "Mempool.space":
                mIsNetworkSupported = isMainnet;
                mUrl = "https://mempool.space/address/" + address;
                break;
            case "Blockchain Reader (Yogh)":
                mIsNetworkSupported = isMainnet;
                mUrl = "https://yogh.io/#addr:id:" + address;
                break;
            case "OXT":
                mIsNetworkSupported = isMainnet;
                mUrl = "https://oxt.me/address/" + address;
                break;
        }

        if (PrefsUtil.isTorEnabled()) {
            // Ask user to confirm risking privacy issues
            new UserGuardian(mContext, this::startBlockExplorer).privacyExternalLink();
        } else {
            startBlockExplorer();
        }
    }

    private void unsupportedNetwork(String explorer, String network, Context ctx) {
        new AlertDialog.Builder(ctx)
                .setTitle(R.string.error_blockExplorer_title)
                .setMessage(ctx.getResources().getString(R.string.error_blockExplorer_message, explorer, network))
                .setCancelable(true)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void startBlockExplorer() {
        if (mIsNetworkSupported) {
            // Call the url
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
            mContext.startActivity(browserIntent);
        } else {
            String explorer = PrefsUtil.getPrefs().getString("blockExplorer", "Blockstream");
            boolean isMainnet = Wallet.getInstance().getNetwork() == Wallet.Network.MAINNET;
            unsupportedNetwork(explorer, isMainnet ? "mainnet" : "testnet", mContext);
        }
    }
}
