package app.michaelwuensch.bitbanana.backends.lnd;

import com.github.lightningnetwork.lnd.lnrpc.StateGrpc;

import io.grpc.CallCredentials;
import io.grpc.Channel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class RemoteLndStateService implements LndStateService {

    private StateGrpc.StateStub asyncStub;

    public RemoteLndStateService(Channel channel, CallCredentials callCredentials) {
        asyncStub = StateGrpc.newStub(channel).withCallCredentials(callCredentials);
    }

    @Override
    public Observable<com.github.lightningnetwork.lnd.lnrpc.SubscribeStateResponse> subscribeState(com.github.lightningnetwork.lnd.lnrpc.SubscribeStateRequest request) {
        return DefaultObservable.createDefault(emitter -> asyncStub.subscribeState(request, new RemoteLndStreamObserver<>(emitter)));
    }

    @Override
    public Single<com.github.lightningnetwork.lnd.lnrpc.GetStateResponse> getState(com.github.lightningnetwork.lnd.lnrpc.GetStateRequest request) {
        return DefaultSingle.createDefault(emitter -> asyncStub.getState(request, new RemoteLndSingleObserver<>(emitter)));
    }

}