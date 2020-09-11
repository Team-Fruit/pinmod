package net.ciebus.pinmod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class PinRenderer {
    private static ArrayList<PinData> pins;

    PinRenderer() {
        pins = new ArrayList<PinData>();
    }

    public static void addPin(double x, double y, double z, String player, int dimId) {
        PinData pin = new PinData(x, y, z, player, dimId);
        pins.add(pin);
    }


    @SubscribeEvent
    public void renderPin(RenderWorldLastEvent event) {
        for (PinData pin : pins) {
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("pinmod", "textures/pin_icon_1.png"));
            Tessellator tess = Tessellator.instance;
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glPushMatrix();

            GL11.glColor3f(254f / 255, 182f / 255, 36f / 255);
            GL11.glTranslated(pin.x - RenderManager.renderPosX, pin.y - RenderManager.renderPosY + 1.6d, pin.z - RenderManager.renderPosZ);

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_CULL_FACE);

            float f3 = ActiveRenderInfo.rotationX;
            float f5 = ActiveRenderInfo.rotationZ;
            float f6 = ActiveRenderInfo.rotationYZ;
            float f7 = ActiveRenderInfo.rotationXY;
            float f4 = ActiveRenderInfo.rotationXZ;

            tess.startDrawing(GL11.GL_QUADS);
            double var12 = 0.3d;
            tess.addVertexWithUV((double) (0 - f3 * var12 - f6 * var12), (double) (0 - f4 * var12), (double) (0 - f5 * var12 - f7 * var12), (double) 1, (double) 1);
            tess.addVertexWithUV((double) (0 - f3 * var12 + f6 * var12), (double) (0 + f4 * var12), (double) (0 - f5 * var12 + f7 * var12), (double) 1, (double) 0);
            tess.addVertexWithUV((double) (0 + f3 * var12 + f6 * var12), (double) (0 + f4 * var12), (double) (0 + f5 * var12 + f7 * var12), (double) 0, (double) 0);
            tess.addVertexWithUV((double) (0 + f3 * var12 - f6 * var12), (double) (0 - f4 * var12), (double) (0 + f5 * var12 - f7 * var12), (double) 0, (double) 1);

            tess.draw();

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glRotated(Math.atan2(f3, f5) / Math.PI * 180d + 90d, 0, 1, 0);
            tess.startDrawing(GL11.GL_TRIANGLES);
            tess.addVertex(0, -1.6d, 0);
            tess.addVertex(-0.05d, -0.35d, 0);
            tess.addVertex(0.05d, -0.35d, 0);
            tess.draw();

            FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
            String str = pin.player;
            float s = 0.016666668F * 0.6666667F * 2;
            GL11.glTranslated(0, 0.5, 0);

            GL11.glScalef(s, -s, s);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(0, 0, 0, 0.5f);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tess.startDrawing(GL11.GL_QUADS);
            tess.addVertex((float) fr.getStringWidth(str) / 2 + 2, -6, 0.01f);
            tess.addVertex(-(float) fr.getStringWidth(str) / 2 - 2, -6, 0.01f);
            tess.addVertex(-(float) fr.getStringWidth(str) / 2 - 2, 3, 0.01f);
            tess.addVertex((float) fr.getStringWidth(str) / 2 + 2, 3, 0.01f);
            tess.draw();


            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            fr.drawString(str, -fr.getStringWidth(str) / 2, 0 * 10 - 1 * 5, 0xFFFFFF);


            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

}

class PinData {
    public double x;
    public double y;
    public double z;
    public String player;
    public int dimId;

    public PinData(double x, double y, double z, String player, int dimId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.player = player;
        this.dimId = dimId;
    }
}