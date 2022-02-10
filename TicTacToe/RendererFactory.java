/**
 * A Renderer factory.
 */
public class RendererFactory {

    public Renderer buildRenderer(String rendererType) {
        Renderer renderer;
        switch (rendererType) {
            case "console":
                renderer = new ConsoleRenderer();
                break;
            case "none":
                renderer = new VoidRenderer();
                break;
            default:
                renderer = null;
        }
        return renderer;
    }
}
