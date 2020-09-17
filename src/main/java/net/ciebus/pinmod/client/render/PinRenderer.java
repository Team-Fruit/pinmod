package net.ciebus.pinmod.client.render;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.ciebus.pinmod.PinManager;
import net.ciebus.pinmod.common.data.PinData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
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
    Vec3 vtmp = Vec3.createVectorHelper(0, 0, 0);

    @SubscribeEvent
    public void renderPin(RenderWorldLastEvent event) {
        float partialTicks = event.partialTicks;
        for (PinData pin : PinManager.pins()) {
            render(pin, partialTicks);
        }
    }

    public void render(PinData pin, float partialTicks) {
        if (Minecraft.getMinecraft().theWorld.provider.dimensionId != pin.dimId) return;

        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
        EntityPlayer p = Minecraft.getMinecraft().thePlayer;

        double dx = p.prevPosX + (p.posX - p.prevPosX) * partialTicks;
        double dy = p.prevPosY + (p.posY - p.prevPosY) * partialTicks - p.yOffset;
        double dz = p.prevPosZ + (p.posZ - p.prevPosZ) * partialTicks;

        GLU.gluProject(
            (float) (pin.x - dx),
            (float) (pin.y - dy),
            (float) (pin.z - dz),
            modelview, projection, viewport, objectCoords);

        Vec3 vec = Vec3.createVectorHelper((float) (pin.x - dx), (float) (pin.y - dy), (float) (pin.z - dz)).normalize();
        Vec3 pvec = Vec3.createVectorHelper(Minecraft.getMinecraft().thePlayer.getLookVec().xCoord + 0.0001d, Minecraft.getMinecraft().thePlayer.getLookVec().yCoord + 0.0001d, Minecraft.getMinecraft().thePlayer.getLookVec().zCoord + 0.0001d);
        //Vec3 pvec = Vec3.createVectorHelper(0,0,0);

        //System.out.println(vec.subtract(pvec).normalize().toString());
        Vec3 ptmpvec = vec.subtract(pvec).normalize();

        Vector3d v = new Vector3d(ptmpvec.xCoord * 1 + 0.0001d, ptmpvec.yCoord * 1 + 0.0001d, ptmpvec.zCoord * 1 + 0.0001d);
        Vector3d pv = new Vector3d(p.getLookVec().xCoord + 0.0001d, 0, p.getLookVec().zCoord + 0.0001d);
        double pvangle = pv.angle(new Vector3d(1 + 0.0001d, 0 + 0.0001d, 0 + 0.0001d));
        double pvangle2 = pv.angle(new Vector3d(0 + 0.0001d, 0 + 0.0001d, 1 + 0.0001d));
        double cpvangle = (pvangle2 / Math.PI) * 180 < 90 ? pvangle : -pvangle;
        //System.out.println((pvangle / Math.PI) * 180 + ":" + (pvangle2 / Math.PI) * 180 + ":" + (cpvangle / Math.PI) * 180);
        Matrix3d m = new Matrix3d();
        m.rotY(cpvangle - Math.PI / 2 + 0.0001d);
        m.transform(v);
        //vtmp.xCoord = v.x;
        //vtmp.yCoord = v.y;

        // v.

        pin.update(v.x, v.y);
        pin.update(vec.xCoord * pvec.xCoord + vec.zCoord * pvec.zCoord > 0);

        Vec3 cpvec = Minecraft.getMinecraft().thePlayer.getLookVec();
        double xzLength = Math.sqrt(cpvec.xCoord * cpvec.xCoord + cpvec.zCoord * cpvec.zCoord);
        cpvec.rotateAroundY((float) (Math.PI / 180) * 45);
        // pin.x = cpvec.xCoord * (1 / xzLength) * 15 + Minecraft.getMinecraft().thePlayer.posX;
        //pin.y = 4;
        //pin.z = cpvec.zCoord * (1 / xzLength) * 15 + Minecraft.getMinecraft().thePlayer.posZ;

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

    @SubscribeEvent
    public void renderTest(RenderGameOverlayEvent.Post event) {
        float partialTicks = event.partialTicks;
        for (PinData pin : PinManager.pins()) {
            double dy = (pin.dy - Minecraft.getMinecraft().displayHeight / 2f);
            Tessellator tess = Tessellator.instance;

            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("pinmod", "textures/pin_icon_1.png"));
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glPushMatrix();
            tess.startDrawing(GL11.GL_QUADS);

            double var12 = 0.3d;

//            tess.draw();

/*
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPointSize(20f);

            GL11.glPointSize(20f);
            tess.startDrawing(GL11.GL_POINTS);
 */
            double scale2d = -pin.dy / pin.dx;
            double scale2dd = -pin.dx / pin.dy;
            double x0 = (event.resolution.getScaledHeight_double() / 2) + ((event.resolution.getScaledWidth_double() / 2) * scale2d);
            double y0 = (event.resolution.getScaledWidth_double() / 2) + (event.resolution.getScaledHeight_double() / 2) * scale2dd;
            double xmax = event.resolution.getScaledHeight_double() / 2 - (event.resolution.getScaledWidth_double() / 2) * scale2d;
            double ymax = event.resolution.getScaledWidth_double() / 2 - (event.resolution.getScaledHeight_double() / 2) * scale2dd;


            if (pin.dx > 0) {
                //tess.addVertex(event.resolution.getScaledWidth(), xmax, 0);
                //右
                tess.addVertexWithUV(event.resolution.getScaledWidth() + 50, xmax + 50, 0, (double) 1, (double) 1);
                tess.addVertexWithUV(event.resolution.getScaledWidth() + 50, xmax - 50, 0, (double) 1, (double) 0);
                tess.addVertexWithUV(event.resolution.getScaledWidth() - 50, xmax - 50, 0, (double) 0, (double) 0);
                tess.addVertexWithUV(event.resolution.getScaledWidth() - 50, xmax + 50, 0, (double) 0, (double) 1);

            } else {
                //tess.addVertex(0, x0, 0);
                //左
                tess.addVertexWithUV(50, x0 + 50, 0, (double) 1, (double) 1);
                tess.addVertexWithUV(50, x0 - 50, 0, (double) 1, (double) 0);
                tess.addVertexWithUV(-50, x0 - 50, 0, (double) 0, (double) 0);
                tess.addVertexWithUV(-50, x0 + 50, 0, (double) 0, (double) 1);

            }
            if (pin.dy > 0) {
                //tess.addVertex(ymax, event.resolution.getScaledHeight(), 0);
                //下
                tess.addVertexWithUV(ymax + 50, event.resolution.getScaledHeight() + 50, 0, (double) 1, (double) 1);
                tess.addVertexWithUV(ymax + 50, event.resolution.getScaledHeight() - 50, 0, (double) 1, (double) 0);
                tess.addVertexWithUV(ymax - 50, event.resolution.getScaledHeight() - 50, 0, (double) 0, (double) 0);
                tess.addVertexWithUV(ymax - 50, event.resolution.getScaledHeight() + 50, 0, (double) 0, (double) 1);
            } else {
                //tess.addVertex(y0, 0, 0);
                //上
                tess.addVertexWithUV(y0 + 50, 50, 0, (double) 1, (double) 1);
                tess.addVertexWithUV(y0 + 50, -50, 0, (double) 1, (double) 0);
                tess.addVertexWithUV(y0 - 50, -50, 0, (double) 0, (double) 0);
                tess.addVertexWithUV(y0 - 50, 50, 0, (double) 0, (double) 1);

            }


            //System.out.println((event.resolution.getScaledWidth_double() / 2));
            //System.out.println(scale2d + ":" + scale2dd + ":" + x0 + ":" + y0 + ":" + xmax + ":" + ymax);

            tess.draw();

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPointSize(100f);
            tess.startDrawing(GL11.GL_LINES);
            tess.addVertex(event.resolution.getScaledWidth() / 2d, event.resolution.getScaledHeight() / 2d, 0);
            tess.addVertex(event.resolution.getScaledWidth() / 2d + pin.dx * 10000, event.resolution.getScaledHeight() / 2d + pin.dy * 10000, 0);
            tess.draw();

            if (!pin.isVisible || pin.dx < 0 || pin.dy < 0 || pin.dx > Minecraft.getMinecraft().displayWidth || pin.dy > Minecraft.getMinecraft().displayHeight) {
                EntityPlayer p = Minecraft.getMinecraft().thePlayer;
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

                GL11.glPopMatrix();
                GL11.glPopAttrib();
            } else {
                if (pin.dx * event.resolution.getScaledHeight() / (float) Minecraft.getMinecraft().displayHeight < 0 || (-dy + Minecraft.getMinecraft().displayHeight / 2f) * event.resolution.getScaledWidth() / (float) Minecraft.getMinecraft().displayWidth < 0)
                    return;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glPointSize(20f);
                tess.startDrawing(GL11.GL_POINTS);
                //  tess.addVertex(pin.dx * event.resolution.getScaledHeight() / (float) Minecraft.getMinecraft().displayHeight, (-dy + Minecraft.getMinecraft().displayHeight / 2f) * event.resolution.getScaledWidth() / (float) Minecraft.getMinecraft().displayWidth, 0);
                tess.draw();
                GL11.glPopMatrix();
                GL11.glPopAttrib();
            }
        }
    }

}
