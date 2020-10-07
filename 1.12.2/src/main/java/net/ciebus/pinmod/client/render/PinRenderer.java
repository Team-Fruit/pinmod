package net.ciebus.pinmod.client.render;

import net.ciebus.pinmod.PinManager;
import net.ciebus.pinmod.common.data.PinData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class PinRenderer {

    private static IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private static FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private static FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private static FloatBuffer objectCoords = GLAllocation.createDirectFloatBuffer(16);

    float ny = 0f;
    Vec3d vtmp = new Vec3d(0, 0, 0);

    @SubscribeEvent
    public void renderPin(RenderWorldLastEvent event) {
        float partialTicks = event.getPartialTicks();
        for (PinData pin : PinManager.pins()) {
            render(pin, partialTicks,event);
        }
    }

    public void render(PinData pin, float partialTicks, RenderWorldLastEvent event) {
        if (Minecraft.getMinecraft().world.provider.getDimension() != pin.dimId) return;

        EntityPlayer p = Minecraft.getMinecraft().player;
        double pinLength = Math.sqrt((p.posX - pin.x) * (p.posX - pin.x) + (p.posY - pin.y) * (p.posY - pin.y) + (p.posZ - pin.z) * (p.posZ - pin.z));

        GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GlStateManager.glGetInteger(GL11.GL_VIEWPORT, viewport);

        double dx = p.prevPosX + (p.posX - p.prevPosX) * partialTicks;
        double dy = p.prevPosY + (p.posY - p.prevPosY) * partialTicks - p.getYOffset();
        double dz = p.prevPosZ + (p.posZ - p.prevPosZ) * partialTicks;

        GLU.gluProject(
                (float) (pin.x - dx),
                (float) (pin.y - dy),
                (float) (pin.z - dz),
                modelview, projection, viewport, objectCoords);
        //pin Vector
        Vec3d vec = new Vec3d((float) (pin.x - dx), (float) (pin.y - dy), (float) (pin.z - dz)).normalize();
        //player Vector
        Vec3d pvec = new Vec3d(Minecraft.getMinecraft().player.getLookVec().x, Minecraft.getMinecraft().player.getLookVec().y, Minecraft.getMinecraft().player.getLookVec().z);


        Vector3d vecd = new Vector3d((float) (pin.x - dx), (float) (pin.y - dy), (float) (pin.z - dz));
        vecd.normalize();
        Vector3d pvecd = new Vector3d(Minecraft.getMinecraft().player.getLookVec().x, Minecraft.getMinecraft().player.getLookVec().y, Minecraft.getMinecraft().player.getLookVec().z);
        pvecd.normalize();

        double bl = vecd.dot(pvecd);
        pvecd.scale(Math.abs(bl));
        Vector3d subvec = new Vector3d();
        subvec.sub(vecd, pvecd);
        Vec3d ptmpvec = new Vec3d(-subvec.x, -subvec.y, -subvec.z);

        Vector3d v = new Vector3d(ptmpvec.x * 1, ptmpvec.y * 1, ptmpvec.z * 1);
        Vector3d pv = new Vector3d(p.getLookVec().x, 0, p.getLookVec().z);
        double pvangle = pv.angle(new Vector3d(1, 0, 0));
        double pvangle2 = pv.angle(new Vector3d(0, 0, 1));
        double cpvangle = (pvangle2 / Math.PI) * 180 < 90 ? pvangle : -pvangle;
        Matrix3d m = new Matrix3d();
        m.rotY(cpvangle - Math.PI / 2);
        m.transform(v);

        pin.update(v.x, v.y);
        pin.update(vec.x * pvec.x + vec.z * pvec.z > 0);

        Vec3d cpvec = Minecraft.getMinecraft().player.getLookVec();
        cpvec.rotateYaw((float) (Math.PI / 180) * 45);

        Tessellator tess = Tessellator.getInstance();
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("pinmod","textures/pin_icon_1.png"));


        GlStateManager.color(254f / 255, 182f / 255, 36f / 255);
        GlStateManager.translate(pin.x - p.posX, pin.y - p.posY + 1.6d, pin.z - p.posZ);

        //GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();
        //GL11.glDisable(GL11.GL_DEPTH_TEST);
        GlStateManager.disableDepth();
        //GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.disableCull();

        float f3 = ActiveRenderInfo.getRotationX();
        float f5 = ActiveRenderInfo.getRotationZ();
        float f6 = ActiveRenderInfo.getRotationYZ();
        float f7 = ActiveRenderInfo.getRotationXY();
        float f4 = ActiveRenderInfo.getRotationXZ();

        //Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tess.getBuffer();

        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        double scale = bl > 0.995 && pinLength > 30 ? 2 * Math.sqrt(pinLength) : 1 * Math.sqrt(Math.sqrt(pinLength * 3));
        double var12 = 0.3d;
        renderer.pos((double) (0 - f3 * var12 - f6 * var12) * scale, (double) (0 - f4 * var12) * scale, (double) (0 - f5 * var12 - f7 * var12) * scale).tex((double) 1, (double) 1).endVertex();
        renderer.pos((double) (0 - f3 * var12 + f6 * var12) * scale, (double) (0 + f4 * var12) * scale, (double) (0 - f5 * var12 + f7 * var12) * scale).tex((double) 1, (double) 0).endVertex();
        renderer.pos((double) (0 + f3 * var12 + f6 * var12) * scale, (double) (0 + f4 * var12) * scale, (double) (0 + f5 * var12 + f7 * var12) * scale).tex((double) 0, (double) 0).endVertex();
        renderer.pos((double) (0 + f3 * var12 - f6 * var12) * scale, (double) (0 - f4 * var12) * scale, (double) (0 + f5 * var12 - f7 * var12) * scale).tex((double) 0, (double) 1).endVertex();
        tess.draw();

        //GL11.glDisable(GL11.GL_TEXTURE_2D);
        GlStateManager.enableTexture2D();
        //GL11.glEnable(GL11.GL_DEPTH_TEST);
        GlStateManager.enableDepth();
        //GL11.glRotated(Math.atan2(f3, f5) / Math.PI * 180d + 90d, 0, 1, 0);
        GlStateManager.rotate((float) Math.atan2(f3, f5) /(float) Math.PI * 180f + 90f, 0, 1, 0);
        //GL11.glDisable(GL11.GL_TEXTURE_2D);
        GlStateManager.disableTexture2D();
        renderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
        renderer.pos(0, -1.6d, 0).endVertex();
        renderer.pos(-0.05d, -0.35d, 0).endVertex();
        renderer.pos(0.05d, -0.35d, 0).endVertex();
        tess.draw();
        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        String str = pin.player + "(" + (int) pinLength + "m)";
        float s = 0.016666668F * 0.6666667F * 2;
        //GL11.glTranslated(0, 0.5, 0);
        GlStateManager.translate(0f,0.5f,0f);

        GlStateManager.scale(s, -s, s);

        //GL11.glEnable(GL11.GL_BLEND);
        GlStateManager.enableBlend();
        GlStateManager.color(0, 0, 0, 0.5f);
        //GL11.glDisable(GL11.GL_TEXTURE_2D);
        GlStateManager.disableTexture2D();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        renderer.pos(((float) fr.getStringWidth(str) / 2 + 2) * scale, -6 * scale, 0.01f);
        renderer.pos((-(float) fr.getStringWidth(str) / 2 - 2) * scale, -6 * scale, 0.01f);
        renderer.pos((-(float) fr.getStringWidth(str) / 2 - 2) * scale, 3 * scale, 0.01f);
        renderer.pos(((float) fr.getStringWidth(str) / 2 + 2) * scale, 3 * scale, 0.01f);
        //renderer.finishDrawing();
        tess.draw();


        //GL11.glEnable(GL11.GL_TEXTURE_2D);
        GlStateManager.enableTexture2D();
        //GL11.glDisable(GL11.GL_DEPTH_TEST);
        //GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.scale((float) scale, (float) scale, (float) scale);
        fr.drawString(str, -fr.getStringWidth(str) / 2, 0 * 10 - 1 * 5, 0xFFFFFF);

        //renderer.finishDrawing();

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    @SubscribeEvent
    public void renderGui(RenderGameOverlayEvent.Post event) {
        float partialTicks = event.getPartialTicks();
        for (PinData pin : PinManager.pins()) {
            double dy = (pin.dy - Minecraft.getMinecraft().displayHeight / 2f);
            Tessellator tess = Tessellator.getInstance();

            //GlStateManager.pushAttrib();
            GlStateManager.pushMatrix();
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("pinmod", "textures/pin_icon_1.png"));

            BufferBuilder renderer = tess.getBuffer();
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

            double scale2d = -pin.dy / pin.dx;
            double scale2dd = -pin.dx / pin.dy;
            double x0 = (event.getResolution().getScaledHeight_double() / 2) + ((event.getResolution().getScaledWidth_double() / 2) * scale2d);
            double y0 = (event.getResolution().getScaledWidth_double() / 2) + (event.getResolution().getScaledHeight_double() / 2) * scale2dd;
            double xmax = event.getResolution().getScaledHeight_double() / 2 - (event.getResolution().getScaledWidth_double() / 2) * scale2d;
            double ymax = event.getResolution().getScaledWidth_double() / 2 - (event.getResolution().getScaledHeight_double() / 2) * scale2dd;

            double guiX = 0d;
            double guiY = 0d;

            double iconsize = 10d;

            GlStateManager.color(254f / 255, 182f / 255, 36f / 255);
            if (scale2d > 0 && scale2d < 2 && pin.dx > 0) {
                guiX = event.getResolution().getScaledWidth() - iconsize;
                guiY = iconsize;
            } else if (scale2d < 0 && scale2d > -2 && pin.dx > 0) {
                guiX = event.getResolution().getScaledWidth() - iconsize;
                guiY = event.getResolution().getScaledHeight() - iconsize;
            } else if (scale2d > 0 && scale2d < 2 && pin.dy > 0) {
                guiX = iconsize;
                guiY = event.getResolution().getScaledHeight() - iconsize;
            } else if (scale2d < 0 && scale2d > -2 && pin.dy < 0) {
                guiX = iconsize;
                guiY = iconsize;
            }

            if (pin.dx > 0) {
                if (xmax < event.getResolution().getScaledHeight() - iconsize && xmax > iconsize) {
                    guiX = event.getResolution().getScaledWidth() - iconsize;
                    guiY = xmax;
                }
            } else {
                if (x0 > iconsize && x0 < event.getResolution().getScaledHeight() - iconsize) {
                    guiX = iconsize;
                    guiY = x0;
                }
            }
            if (pin.dy > 0) {
                if (ymax > iconsize && ymax < event.getResolution().getScaledWidth() - iconsize) {
                    guiX = ymax;
                    guiY = event.getResolution().getScaledHeight() - iconsize;
                }
            } else {
                if (y0 > iconsize && y0 < event.getResolution().getScaledWidth() - iconsize) {
                    guiX = y0;
                    guiY = iconsize;
                }
            }
            renderer.pos(guiX + iconsize, guiY + iconsize, 0).tex((double) 1, (double) 1).endVertex();
            renderer.pos(guiX + iconsize, guiY - iconsize, 0).tex((double) 1, (double) 0).endVertex();
            renderer.pos(guiX - iconsize, guiY - iconsize, 0).tex((double) 0, (double) 0).endVertex();
            renderer.pos(guiX - iconsize, guiY + iconsize, 0).tex((double) 0, (double) 1).endVertex();

            tess.draw();

            /*
            GlStateManager.disableTexture2D();
            //GL11.glDisable(GL11.GL_TEXTURE_2D);
            GlStateManager.
            GL11.glPointSize(100f);
            //renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_TEX);
            //tess.draw();

             */
            //GL11.glEnable(GL11.GL_TEXTURE_2D);
            //GlStateManager.enableTexture2D();
            GlStateManager.depthMask(false);

            EntityPlayer p = Minecraft.getMinecraft().player;

            double pinLength = Math.sqrt((p.posX - pin.x) * (p.posX - pin.x) + (p.posY - pin.y) * (p.posY - pin.y) + (p.posZ - pin.z) * (p.posZ - pin.z));

            FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
            String str = pin.player + "(" + (int) pinLength + "m)";
            if (event.getResolution().getScaledWidth() - guiX > fr.getStringWidth(str)) {
                fr.drawString(str, (int) guiX + 10, (int) (guiY - iconsize / 2 + 2), 0xFFFFFF);
            } else {
                fr.drawString(str, (int)(guiX - iconsize - fr.getStringWidth(str)), (int) (guiY - iconsize / 2 + 2), 0xFFFFFF);
            }

            /*
            if (!pin.isVisible || pin.dx < 0 || pin.dy < 0 || pin.dx > Minecraft.getMinecraft().displayWidth || pin.dy > Minecraft.getMinecraft().displayHeight) {


                // EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                double ddx = p.prevPosX + (p.posX - p.prevPosX) * partialTicks;
                double ddy = p.prevPosY + (p.posY - p.prevPosY) * partialTicks - p.getYOffset();
                double ddz = p.prevPosZ + (p.posZ - p.prevPosZ) * partialTicks;

                Vec3d vec = new Vec3d((float) (pin.x - ddx), (float) (pin.y - dy), (float) (pin.z - ddz)).normalize();
                //Vec3 pvec = Minecraft.getMinecraft().renderViewEntity.rayTrace(200, 1.0F).hitVec.normalize();
                Vec3d pvec = Minecraft.getMinecraft().player.getLookVec().normalize();
                Vec3d rpvec = Minecraft.getMinecraft().player.getLookVec().normalize();
                rpvec.rotateYaw(-90f);


                double pcos = (vec.x * pvec.x + vec.z * pvec.z);
                double rpcos = (vec.x * rpvec.x + vec.z * rpvec.z);
                // System.out.println((vec.xCoord * pvec.xCoord + vec.zCoord * pvec.zCoord) + ":" + (vec.xCoord * rpvec.xCoord + vec.zCoord * rpvec.zCoord));
            } else {
                if (pin.dx * event.getResolution().getScaledHeight() / (float) Minecraft.getMinecraft().displayHeight < 0 || (-dy + Minecraft.getMinecraft().displayHeight / 2f) * event.getResolution().getScaledWidth() / (float) Minecraft.getMinecraft().displayWidth < 0)
                    return;
                //GL11.glDisable(GL11.GL_TEXTURE_2D);
                GlStateManager.disableTexture2D();
                //GL11.glPointSize(20f);
                // tess.startDrawing(GL11.GL_POINTS);
                //tess.draw();
            }

             */
            GlStateManager.depthMask(true);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
            //GlStateManager.popAttrib();
        }
    }

}
