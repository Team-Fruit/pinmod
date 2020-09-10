package net.ciebus.pinmod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

@Mod(modid = Pin.MODID, version = Pin.VERSION)
public class Pin {
    public static final String MODID = "pin";
    public static final String VERSION = "1.0";
    public static boolean flag = false;
    public static Vec3 position = null;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
        System.out.println("DIRT BLOCK >> " + Blocks.dirt.getUnlocalizedName());
    }


    @EventHandler
    public void load(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SidedProxy(clientSide = "net.ciebus.pinmod.ClientProxy", serverSide = "net.ciebus.pinmod.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PacketHandler.init();
    }

    @SubscribeEvent
    public void tickServer(RenderWorldLastEvent event) {
        // System.out.println("sushi3");
        if (flag) {
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("pinmod", "textures/pin_icon_1.png"));
            Tessellator tess = Tessellator.instance;
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glPushMatrix();

            GL11.glColor3f(254f / 255, 182f / 255, 36f / 255);
            GL11.glTranslated(position.xCoord - RenderManager.renderPosX, position.yCoord - RenderManager.renderPosY + 1.6d, position.zCoord - RenderManager.renderPosZ);

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
            tess.addVertexWithUV((double)(0 - f3 * var12 - f6 * var12), (double)(0 - f4 * var12), (double)(0 - f5 * var12 - f7 * var12), (double)1, (double)1);
            tess.addVertexWithUV((double)(0 - f3 * var12 + f6 * var12), (double)(0 + f4 * var12), (double)(0 - f5 * var12 + f7 * var12), (double)1, (double)0);
            tess.addVertexWithUV((double)(0 + f3 * var12 + f6 * var12), (double)(0 + f4 * var12), (double)(0 + f5 * var12 + f7 * var12), (double)0, (double)0);
            tess.addVertexWithUV((double)(0 + f3 * var12 - f6 * var12), (double)(0 - f4 * var12), (double)(0 + f5 * var12 - f7 * var12), (double)0, (double)1);

            tess.draw();

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glRotated(Math.atan2(f3,f5) / Math.PI * 180d + 90d,0,1,0);
            tess.startDrawing(GL11.GL_TRIANGLES);
            tess.addVertex(0,-1.6d,0);
            tess.addVertex(-0.05d,-0.35d,0);
            tess.addVertex(0.05d,-0.35d,0);
            tess.draw();

            FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
            String str = "kokoa0429";
            float s = 0.016666668F * 0.6666667F * 2;
            GL11.glTranslated(0, 0.5, 0);

            GL11.glScalef(s, -s, s);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(0, 0, 0, 0.5f);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tess.startDrawing(GL11.GL_QUADS);
            tess.addVertex((float)fr.getStringWidth(str) / 2 + 2,-6,0.01f);
            tess.addVertex(-(float)fr.getStringWidth(str) / 2 - 2,-6,0.01f);
            tess.addVertex(-(float)fr.getStringWidth(str) / 2 - 2,3,0.01f);
            tess.addVertex((float)fr.getStringWidth(str) / 2 + 2,3,0.01f);
            tess.draw();


            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            fr.drawString(str, -fr.getStringWidth(str) / 2, 0 * 10 - 1 * 5, 0xFFFFFF);



            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    @SubscribeEvent
    public void KeyHandlingEvent(InputEvent.KeyInputEvent event) {
        if (ClientProxy.pinKey.isPressed()) {
            MovingObjectPosition mop = Minecraft.getMinecraft().renderViewEntity.rayTrace(200, 1.0F);
            if (mop != null) {
                //some data to server
                PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed((byte) 0x01));
                System.out.println(mop.hitVec);
                position = mop.hitVec;
                flag = true;
            }
        }
    }
}
