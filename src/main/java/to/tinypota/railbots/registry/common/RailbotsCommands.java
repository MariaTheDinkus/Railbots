package to.tinypota.railbots.registry.common;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class RailbotsCommands {
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
//			dispatcher.register(literal("getcore")
//					.then(argument("player", EntityArgumentType.player())
//							.executes(context -> {
//								var player = EntityArgumentType.getPlayer(context, "player");
//								var playerState = RailbotsServerState.getPlayerState(context.getSource().getServer(), player);
//								context.getSource().sendFeedback(() -> Text.literal("Their current preferred core is " + playerState.getSuitedCore()), false);
//								return 1;
//							})
//					)
//			);
//
//			dispatcher.register(literal("setcore")
//					.then(argument("player", EntityArgumentType.player())
//							.then(argument("core", IdentifierArgumentType.identifier())
//									.suggests(AVAILABLE_CORES)
//									.executes(context -> {
//										var player = EntityArgumentType.getPlayer(context, "player");
//										var playerState = RailbotsServerState.getPlayerState(context.getSource().getServer(), player);
//										var coreId = IdentifierArgumentType.getIdentifier(context, "core");
//										// Ensure the core exists
//										var core = RailbotsRegistries.CORE.get(coreId);
//										if (core == null) {
//											context.getSource().sendFeedback(() -> Text.translatable("command.railbots.setcore.fail", coreId), false);
//											return 0;
//										}
//										playerState.setSuitedCore(coreId.toString());
//										context.getSource().sendFeedback(() -> Text.translatable("command.railbots.setcore.success", player.getDisplayName(), Text.translatable(core.getTranslationKey())), true);
//										return 1;
//									})
//							)
//					)
//			);
		});
	}
}
