package to.tinypota.railbots.registry.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import to.tinypota.railbots.api.RailGraph;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class RailbotsEvents {
	public static RailGraph railGraph = new RailGraph();
	
	public static void init() {
		WorldRenderEvents.END.register(context -> {
			// Detect if player has hitboxes shown
			if (FabricLoader.getInstance().isDevelopmentEnvironment() && MinecraftClient.getInstance().options.advancedItemTooltips) {
				var camera = context.camera();
				
				var matrixStack = new MatrixStack();
				matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
				matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
				
				var checked = new ArrayList<Map.Entry<BlockPos, BlockPos>>();
				
				for (var entry : railGraph.getGraph().entries()) {
					var found = false;
					// Check if opposite entry exists
					for (var entry2 : checked) {
						if (entry.getKey().equals(entry2.getValue()) && entry.getValue().equals(entry2.getKey())) {
							found = true;
							break;
						}
					}
					
					if (!found && entry.getKey().isWithinDistance(camera.getPos(), 32)) {
						matrixStack.push();
						var source = entry.getKey();
						var target = entry.getValue();
						
						var tessellator = Tessellator.getInstance();
						var buffer = tessellator.getBuffer();
						
						var positionMatrix = matrixStack.peek().getPositionMatrix();
						var targetPosition = new Vec3d(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5);
						var transformedPosition = targetPosition.subtract(camera.getPos());
						matrixStack.translate(transformedPosition.x, transformedPosition.y, transformedPosition.z);
						
						var start = targetPosition.subtract(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5);
						
						buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
						buffer.vertex(positionMatrix, 0, 0, 0).color(255, 0, 0, 255).next();
						buffer.vertex(positionMatrix, (float) -start.x, (float) -start.y, (float) -start.z).color(255, 0, 0, 255).next();
						
						RenderSystem.setShader(GameRenderer::getPositionColorProgram);
						RenderSystem.disableCull();
						RenderSystem.depthFunc(GL11.GL_ALWAYS);
						
						tessellator.draw();
						
						RenderSystem.depthFunc(GL11.GL_LEQUAL);
						RenderSystem.enableCull();
						matrixStack.pop();
						checked.add(entry);
					}
				}
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
