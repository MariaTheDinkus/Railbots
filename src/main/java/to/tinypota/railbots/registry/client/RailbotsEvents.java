package to.tinypota.railbots.registry.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import to.tinypota.railbots.api.RailGraph;
import to.tinypota.railbots.common.entity.RailbotEntity;
import to.tinypota.railbots.registry.common.RailbotsEntityTypes;

import java.awt.*;
import java.util.*;
import java.util.List;

public class RailbotsEvents {
	public static RailGraph railGraph = new RailGraph();
	
	public static void init() {
		WorldRenderEvents.END.register(context -> {
			if (FabricLoader.getInstance().isDevelopmentEnvironment() && MinecraftClient.getInstance().options.advancedItemTooltips) {
				var camera = context.camera();
				var cameraPos = camera.getPos();
				var matrixStack = new MatrixStack();
				RenderSystem.setShader(GameRenderer::getPositionColorProgram);
				RenderSystem.disableCull();
				RenderSystem.depthFunc(GL11.GL_ALWAYS);
				
				matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
				matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
				
				Set<BlockPos> checked = new HashSet<>();
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				
				// Fetch all RailBotEntities within a 25-block radius
				List<RailbotEntity> railBots = MinecraftClient.getInstance().world.getEntitiesByType(RailbotsEntityTypes.RAILBOT, new Box(MinecraftClient.getInstance().player.getPos().add(-42, -42, -42), MinecraftClient.getInstance().player.getPos().add(32, 32, 32)), entity -> true);
				
				for (var entry : railGraph.getGraph().entries()) {
					var source = entry.getKey();
					var target = entry.getValue();
					
					if (source.isWithinDistance(cameraPos, 32)) {
						matrixStack.push();
						Vec3d sourceVec = new Vec3d(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5);
						Vec3d targetVec = new Vec3d(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5);
						Vec3d transformedSource = sourceVec.subtract(cameraPos);
						Vec3d transformedTarget = targetVec.subtract(cameraPos);
						
						matrixStack.translate(transformedSource.x, transformedSource.y, transformedSource.z);
						Matrix4f positionMatrix = matrixStack.peek().getPositionMatrix();
						Vec3d start = targetVec.subtract(sourceVec);
						
						// Calculate distance to the nearest RailBotEntity and determine color
						double minDistance = railBots.stream()
													 .mapToDouble(bot -> sourceVec.distanceTo(bot.getPos()))
													 .min()
													 .orElse(32);
						double normalizedDistance = Math.min(minDistance / 32.0, 1.0);
						float[] rgb = getRGBFromPercentage(Math.max(0, 1 - normalizedDistance - 0.25));
						
						buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
						buffer.vertex(positionMatrix, 0, 0, 0).color(rgb[0], rgb[1], rgb[2], 1.0f).next();
						buffer.vertex(positionMatrix, (float) start.x, (float) start.y, (float) start.z).color(rgb[0], rgb[1], rgb[2], 1.0f).next();
						tessellator.draw();
						
						int connections = railGraph.getConnectedRails(source).size();
						if (connections > 2) {
							BlockPos position = entry.getKey();
							Vec3d posVec = new Vec3d(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5);
							Vec3d transformedPos = posVec.subtract(cameraPos);
							
							matrixStack.push();
							//matrixStack.translate(transformedPos.x, transformedPos.y, transformedPos.z);
							positionMatrix = matrixStack.peek().getPositionMatrix();
							
							buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
							float white = 1.0f;
							float size = 0.05f;
							buffer.vertex(positionMatrix, -size, -size, 0).color(white, white, white, 1.0f).next();
							buffer.vertex(positionMatrix, size, -size, 0).color(white, white, white, 1.0f).next();
							buffer.vertex(positionMatrix, size, size, 0).color(white, white, white, 1.0f).next();
							buffer.vertex(positionMatrix, -size, size, 0).color(white, white, white, 1.0f).next();
							tessellator.draw();
							
							matrixStack.push();
							matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
							positionMatrix = matrixStack.peek().getPositionMatrix();
							
							buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
							buffer.vertex(positionMatrix, -size, -size, 0).color(white, white, white, 1.0f).next();
							buffer.vertex(positionMatrix, size, -size, 0).color(white, white, white, 1.0f).next();
							buffer.vertex(positionMatrix, size, size, 0).color(white, white, white, 1.0f).next();
							buffer.vertex(positionMatrix, -size, size, 0).color(white, white, white, 1.0f).next();
							tessellator.draw();
							matrixStack.pop();
							
							matrixStack.push();
							matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
							positionMatrix = matrixStack.peek().getPositionMatrix();
							
							buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
							buffer.vertex(positionMatrix, -size, -size, 0).color(white, white, white, 1.0f).next();
							buffer.vertex(positionMatrix, size, -size, 0).color(white, white, white, 1.0f).next();
							buffer.vertex(positionMatrix, size, size, 0).color(white, white, white, 1.0f).next();
							buffer.vertex(positionMatrix, -size, size, 0).color(white, white, white, 1.0f).next();
							tessellator.draw();
							matrixStack.pop();
							
							matrixStack.pop();
						}
						
						matrixStack.pop();
						checked.add(target);
					}
				}
				
				RenderSystem.depthFunc(GL11.GL_LEQUAL);
				RenderSystem.enableCull();
			}
		});
	}
	
	public static float[] getRGBFromPercentage(double castPercentage) {
		// minHue and maxHue define the color range for the transition
		// adjust these values to achieve the desired color range
		var minHue = 0.00f;  // slightly more than pure red
		var maxHue = 0.28f;  // slightly less than pure green
		
		// interpolate between minHue and maxHue based on the castPercentage
		var hue = minHue + (float) castPercentage * (maxHue - minHue);
		
		var saturation = 1.0f;
		var brightness = 1.0f;
		
		var rgb = Color.HSBtoRGB(hue, saturation, brightness);
		
		var red = ((rgb >> 16) & 0xFF) / 255.0f;
		var green = ((rgb >> 8) & 0xFF) / 255.0f;
		var blue = (rgb & 0xFF) / 255.0f;
		
		return new float[]{red, green, blue};
	}
}
