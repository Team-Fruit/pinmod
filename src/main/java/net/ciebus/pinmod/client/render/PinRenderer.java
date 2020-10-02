package net.ciebus.pinmod.client.render;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.ciebus.pinmod.PinManager;
import net.ciebus.pinmod.common.data.PinData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class PinRenderer {

    private static FloatBuffer viewport = GLAllocation.createDirectFloatBuffer(16);
    private static FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private static FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private static FloatBuffer objectCoords = GLAllocation.createDirectFloatBuffer(16);

    float ny = 0f;
    Vec3 vtmp = Vec3.createVectorHelper(0, 0, 0);

    @SubscribeEvent
    public void renderPin(RenderWorldLastEvent event) {
        float partialTicks = event.partialTicks;
        for (PinData pin : PinManager.pins()) {
            render(pin, partialTicks);
        }
    }

    public void render(PinData pin, float partialTicks) {
        if (Minecraft.getInstance().world.dimension. .dimensionId != pin.dimId)return;

        EntityPlayer p = Minecraft.getMinecraft().thePlayer;
        double pinLength = Math.sqrt((p.posX - pin.x) * (p.posX - pin.x) + (p.posY - pin.y) * (p.posY - pin.y) + (p.posZ - pin.z) * (p.posZ - pin.z));

        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

        double dx = p.prevPosX + (p.posX - p.prevPosX) * partialTicks;
        double dy = p.prevPosY + (p.posY - p.prevPosY) * partialTicks - p.yOffset;
        double dz = p.prevPosZ + (p.posZ - p.prevPosZ) * partialTicks;

        GLU.gluProject(
                (float) (pin.x - dx),
                (float) (pin.y - dy),
                (float) (pin.z - dz),
                modelview, projection, viewport, objectCoords);
        //pin Vector
        Vec3 vec = Vec3.createVectorHelper((float) (pin.x - dx), (float) (pin.y - dy), (float) (pin.z - dz)).normalize();
        //player Vector
        Vec3 pvec = Vec3.createVectorHelper(Minecraft.getMinecraft().thePlayer.getLookVec().xCoord, Minecraft.getMinecraft().thePlayer.getLookVec().yCoord, Minecraft.getMinecraft().thePlayer.getLookVec().zCoord);


        Vector3d vecd = new Vector3d((float) (pin.x - dx), (float) (pin.y - dy), (float) (pin.z - dz));
        vecd.normalize();
        Vector3d pvecd = new Vector3d(Minecraft.getMinecraft().thePlayer.getLookVec().xCoord, Minecraft.getMinecraft().thePlayer.getLookVec().yCoord, Minecraft.getMinecraft().thePlayer.getLookVec().zCoord);
        pvecd.normalize();

        double bl = vecd.dot(pvecd);
        pvecd.scale(Math.abs(bl));
        Vector3d subvec = new Vector3d();
        subvec.sub(vecd, pvecd);
        Vec3 ptmpvec = Vec3.createVectorHelper(-subvec.x, -subvec.y, -subvec.z);

        Vector3d v = new Vector3d(ptmpvec.xCoord * 1, ptmpvec.yCoord * 1, ptmpvec.zCoord * 1);
        Vector3d pv = new Vector3d(p.getLookVec().xCoord, 0, p.getLookVec().zCoord);
        double pvangle = pv.angle(new Vector3d(1, 0, 0));
        double pvangle2 = pv.angle(new Vector3d(0, 0, 1));
        double cpvangle = (pvangle2 / Math.PI) * 180 < 90 ? pvangle : -pvangle;
        Matrix3d m = new Matrix3d();
        m.rotY(cpvangle - Math.PI / 2);
        m.transform(v);

        pin.update(v.x, v.y);
        pin.update(vec.xCoord * pvec.xCoord + vec.zCoord * pvec.zCoord > 0);

        Vec3 cpvec = Minecraft.getMinecraft().thePlayer.getLookVec();
        cpvec.rotateAroundY((float) (Math.PI / 180) * 45);

        Tessellator tess = Tessellator.instance;
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("pinmod", "textures/pin_icon_1.png"));


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
        double scale = bl > 0.995 && pinLength > 30 ? 2 * Math.sqrt(pinLength) : 1 * Math.sqrt(Math.sqrt(pinLength * 3));
        double var12 = 0.3d;
        tess.addVertexWithUV((double) (0 - f3 * var12 - f6 * var12) * scale, (double) (0 - f4 * var12) * scale, (double) (0 - f5 * var12 - f7 * var12) * scale, (double) 1, (double) 1);
        tess.addVertexWithUV((double) (0 - f3 * var12 + f6 * var12) * scale, (double) (0 + f4 * var12) * scale, (double) (0 - f5 * var12 + f7 * var12) * scale, (double) 1, (double) 0);
        tess.addVertexWithUV((double) (0 + f3 * var12 + f6 * var12) * scale, (double) (0 + f4 * var12) * scale, (double) (0 + f5 * var12 + f7 * var12) * scale, (double) 0, (double) 0);
        tess.addVertexWithUV((double) (0 + f3 * var12 - f6 * var12) * scale, (double) (0 - f4 * var12) * scale, (double) (0 + f5 * var12 - f7 * var12) * scale, (double) 0, (double) 1);

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
        String str = pin.player + "(" + (int) pinLength + "m)";
        float s = 0.016666668F * 0.6666667F * 2;
        GL11.glTranslated(0, 0.5, 0);

        GL11.glScalef(s, -s, s);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(0, 0, 0, 0.5f);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tess.startDrawing(GL11.GL_QUADS);
        tess.addVertex(((float) fr.getStringWidth(str) / 2 + 2) * scale, -6 * scale, 0.01f);
        tess.addVertex((-(float) fr.getStringWidth(str) / 2 - 2) * scale, -6 * scale, 0.01f);
        tess.addVertex((-(float) fr.getStringWidth(str) / 2 - 2) * scale, 3 * scale, 0.01f);
        tess.addVertex(((float) fr.getStringWidth(str) / 2 + 2) * scale, 3 * scale, 0.01f);
        tess.draw();


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glScalef((float) scale, (float) scale, (float) scale);
        fr.drawString(str, -fr.getStringWidth(str) / 2, 0 * 10 - 1 * 5, 0xFFFFFF);


        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    @SubscribeEvent
    public void renderGui(RenderGameOverlayEvent.Post event) {
        float partialTicks = event.partialTicks;
        for (PinData pin : PinManager.pins()) {
            double dy = (pin.dy - Minecraft.getMinecraft().displayHeight / 2f);
            Tessellator tess = Tessellator.instance;

            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glPushMatrix();
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("pinmod", "textures/pin_icon_1.png"));
            tess.startDrawing(GL11.GL_QUADS);

            double scale2d = -pin.dy / pin.dx;
            double scale2dd = -pin.dx / pin.dy;
            double x0 = (event.resolution.getScaledHeight_double() / 2) + ((event.resolution.getScaledWidth_double() / 2) * scale2d);
            double y0 = (event.resolution.getScaledWidth_double() / 2) + (event.resolution.getScaledHeight_double() / 2) * scale2dd;
            double xmax = event.resolution.getScaledHeight_double() / 2 - (event.resolution.getScaledWidth_double() / 2) * scale2d;
            double ymax = event.resolution.getScaledWidth_double() / 2 - (event.resolution.getScaledHeight_double() / 2) * scale2dd;

            double guiX = 0d;
            double guiY = 0d;

            double iconsize = 10d;

            GL11.glColor3f(254f / 255, 182f / 255, 36f / 255);
            if (scale2d > 0 && scale2d < 2 && pin.dx > 0) {
                guiX = event.resolution.getScaledWidth() - iconsize;
                guiY = iconsize;
            } else if (scale2d < 0 && scale2d > -2 && pin.dx > 0) {
                guiX = event.resolution.getScaledWidth() - iconsize;
                guiY = event.resolution.getScaledHeight() - iconsize;
            } else if (scale2d > 0 && scale2d < 2 && pin.dy > 0) {
                guiX = iconsize;
                guiY = event.resolution.getScaledHeight() - iconsize;
            } else if (scale2d < 0 && scale2d > -2 && pin.dy < 0) {
                guiX = iconsize;
                guiY = iconsize;
            }

            if (pin.dx > 0) {
                if (xmax < event.resolution.getScaledHeight() - iconsize && xmax > iconsize) {
                    guiX = event.resolution.getScaledWidth() - iconsize;
                    guiY = xmax;
                }
            } else {
                if (x0 > iconsize && x0 < event.resolution.getScaledHeight() - iconsize) {
                    guiX = iconsize;
                    guiY = x0;
                }
            }
            if (pin.dy > 0) {
                if (ymax > iconsize && ymax < event.resolution.getScaledWidth() - iconsize) {
                    guiX = ymax;
                    guiY = event.resolution.getScaledHeight() - iconsize;
                }
            } else {
                if (y0 > iconsize && y0 < event.resolution.getScaledWidth() - iconsize) {
                    guiX = y0;
                    guiY = iconsize;
                }
            }
            tess.addVertexWithUV(guiX + iconsize, guiY + iconsize, 0, (double) 1, (double) 1);
            tess.addVertexWithUV(guiX + iconsize, guiY - iconsize, 0, (double) 1, (double) 0);
            tess.addVertexWithUV(guiX - iconsize, guiY - iconsize, 0, (double) 0, (double) 0);
            tess.addVertexWithUV(guiX - iconsize, guiY + iconsize, 0, (double) 0, (double) 1);

            tess.draw();

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPointSize(100f);
            tess.startDrawing(GL11.GL_LINES);
            tess.draw();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            EntityPlayer p = Minecraft.getMinecraft().thePlayer;

            double pinLength = Math.sqrt((p.posX - pin.x) * (p.posX - pin.x) + (p.posY - pin.y) * (p.posY - pin.y) + (p.posZ - pin.z) * (p.posZ - pin.z));

            FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
            String str = pin.player + "(" + (int) pinLength + "m)";
            if (event.resolution.getScaledWidth() - guiX > fr.getStringWidth(str)) {
                fr.drawString(str, (int) guiX + 10, (int) (guiY - iconsize / 2 + 2), 0xFFFFFF);
            } else {
                fr.drawString(str, (int) (guiX - iconsize - fr.getStringWidth(str)), (int) (guiY - iconsize / 2 + 2), 0xFFFFFF);
            }

            if (!pin.isVisible || pin.dx < 0 || pin.dy < 0 || pin.dx > Minecraft.getMinecraft().displayWidth || pin.dy > Minecraft.getMinecraft().displayHeight) {


                // EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                double ddx = p.prevPosX + (p.posX - p.prevPosX) * partialTicks;
                double ddy = p.prevPosY + (p.posY - p.prevPosY) * partialTicks - p.yOffset;
                double ddz = p.prevPosZ + (p.posZ - p.prevPosZ) * partialTicks;

                Vec3 vec = Vec3.createVectorHelper((float) (pin.x - ddx), (float) (pin.y - dy), (float) (pin.z - ddz)).normalize();
                //Vec3 pvec = Minecraft.getMinecraft().renderViewEntity.rayTrace(200, 1.0F).hitVec.normalize();
                Vec3 pvec = Minecraft.getMinecraft().thePlayer.getLookVec().normalize();
                Vec3 rpvec = Minecraft.getMinecraft().thePlayer.getLookVec().normalize();
                rpvec.rotateAroundY(-90f);


                double pcos = (vec.xCoord * pvec.xCoord + vec.zCoord * pvec.zCoord);
                double rpcos = (vec.xCoord * rpvec.xCoord + vec.zCoord * rpvec.zCoord);
                // System.out.println((vec.xCoord * pvec.xCoord + vec.zCoord * pvec.zCoord) + ":" + (vec.xCoord * rpvec.xCoord + vec.zCoord * rpvec.zCoord));
            } else {
                if (pin.dx * event.resolution.getScaledHeight() / (float) Minecraft.getMinecraft().displayHeight < 0 || (-dy + Minecraft.getMinecraft().displayHeight / 2f) * event.resolution.getScaledWidth() / (float) Minecraft.getMinecraft().displayWidth < 0)
                    return;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glPointSize(20f);
                tess.startDrawing(GL11.GL_POINTS);
                tess.draw();
            }
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

}
